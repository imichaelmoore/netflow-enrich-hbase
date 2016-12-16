FROM ubuntu:xenial

RUN apt-get update
RUN apt-get -y install softflowd gzip axel wget pigz

WORKDIR /opt
#RUN axel -a https://www.ll.mit.edu/ideval/data/1999/training/week2/monday/inside.tcpdump.gz
#RUN wget -O capture.pcap.gz "https://wiki.wireshark.org/SampleCaptures?action=AttachFile&do=get&target=c05-http-reply-r1.pcap.gz" 
#RUN wget -O capture.pcap "https://wiki.wireshark.org/SampleCaptures?action=AttachFile&do=get&target=http-chunked-gzip.pcap"
COPY capture.pcap.gz /opt/example-capture.pcap.gz
RUN pigz -d /opt/example-capture.pcap.gz

CMD softflowd -r /opt/example-capture.pcap -n collector:50000 -v
