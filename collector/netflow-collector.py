#!/usr/bin/env python
# Netflow Collector sourced from http://www.mindrot.org/misc/netflow-collector.py, 
# and modified to send flows as JSON to Apache Kafka by Michael Moore in 2016.

import socket, select, struct
import json

from kafka import KafkaProducer
producer = KafkaProducer(bootstrap_servers='kafka:9092', api_version="0.8.1")


class Flow(object):
	# Virtual base class
	LENGTH = 0
	def __init__(self, data):
		if len(data) != self.LENGTH:
			raise ValueError, "Short flow"

	def _int_to_ipv4(self, addr):
		return "%d.%d.%d.%d" % \
		   (addr >> 24 & 0xff, addr >> 16 & 0xff, \
		    addr >> 8 & 0xff, addr & 0xff)

class Header(object):
	# Virtual base class
	LENGTH = 0
	def __init__(self, data):
		if len(data) != self.LENGTH:
			raise ValueError, "Short flow header"

class Header1(Header):
	LENGTH = struct.calcsize("!HHIII")
	def __init__(self, data):
		if len(data) != self.LENGTH:
			raise ValueError, "Short flow header"
			
		_nh = struct.unpack("!HHIII", data)
		self.version = _nh[0]
		self.num_flows = _nh[1]
		self.sys_uptime = _nh[2]
		self.time_secs = _nh[3]
		self.time_nsecs = _nh[4]

	def __str__(self):
		ret  = "NetFlow Header v.%d containing %d flows\n" % \
		    (self.version, self.num_flows)
		ret += "    Router uptime: %d\n" % self.sys_uptime
		ret += "    Current time:  %d.%09d\n" % \
		    (self.time_secs, self.time_nsecs)

		return ret

class Flow1(Flow):
	LENGTH = struct.calcsize("!IIIHHIIIIHHHBBBBBBI")
	def __init__(self, data):
		if len(data) != self.LENGTH:
			raise ValueError, "Short flow"
			
		_ff = struct.unpack("!IIIHHIIIIHHHBBBBBBI", data)
		self.src_addr = self._int_to_ipv4(_ff[0])
		self.dst_addr = self._int_to_ipv4(_ff[1])
		self.next_hop = self._int_to_ipv4(_ff[2])
		self.in_index = _ff[3]
		self.out_index = _ff[4]
		self.packets = _ff[5]
		self.octets = _ff[6]
		self.start = _ff[7]
		self.finish = _ff[8]
		self.src_port = _ff[9]
		self.dst_port = _ff[10]
		# pad
		self.protocol = _ff[12]
		self.tos = _ff[13]
		self.tcp_flags = _ff[14]

	def __str__(self):
		return {'protocol': self.protocol, 'src_addr': self.src_addr, 'src_port': self.src_port, 'dst_addr': self.dst_addr, 'dst_port': self.dst_port, 'octets': self.octets}

class NetFlowPacket:
	FLOW_TYPES = {
		1 : (Header1, Flow1),
	}
	def __init__(self, data):
		if len(data) < 16:
			raise ValueError, "Short packet"
		_nf = struct.unpack("!H", data[:2])
		self.version = _nf[0]

		if not self.version in self.FLOW_TYPES.keys():
			raise RuntimeWarning, \
			    "NetFlow version %d is not yet implemented" % \
			    self.version
		hdr_class = self.FLOW_TYPES[self.version][0]
		flow_class = self.FLOW_TYPES[self.version][1]

		self.hdr = hdr_class(data[:hdr_class.LENGTH])

		if len(data) - self.hdr.LENGTH != \
		   (self.hdr.num_flows * flow_class.LENGTH):
			raise ValueError, "Packet truncated in flow data"
		
		self.flows = []
		for n in range(self.hdr.num_flows):
			offset = self.hdr.LENGTH + (flow_class.LENGTH * n)
			flow_data = data[offset:offset + flow_class.LENGTH]
			self.flows.append(flow_class(flow_data))

	def __str__(self):
		ret = str(self.hdr)
		i = 0
		for flow in self.flows:
			ret += "Flow %d: " % i
			ret += "%s\n" % str(flow)
			i += 1

		return ret

host = "collector"
port = 50000

addrs = socket.getaddrinfo(host, port, socket.AF_UNSPEC, 
    socket.SOCK_DGRAM, 0, socket.AI_PASSIVE)
socks = []

for addr in addrs:
	sock = socket.socket(addr[0], addr[1])
	sock.bind(addr[4])
	socks.append(sock)

	print "-========================- NETFLOW COLLECTOR READY on [%s]:%d" % (addr[4][0], addr[4][1])

while 1:
	(rlist, wlist, xlist) = select.select(socks, [], socks)

	for sock in rlist:
		(data, addrport) = sock.recvfrom(8192)
		print "Received flow packet from %s:%d" % addrport
		nfp = NetFlowPacket(data)
		for flow in nfp.flows:
			producer.send('netflow', json.dumps(flow.__str__()))
			print(json.dumps(flow.__str__()))


