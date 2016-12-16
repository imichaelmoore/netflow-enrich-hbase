FROM ubuntu:xenial

RUN apt-get update
RUN apt-get -y install softflowd gzip axel wget pigz

WORKDIR /opt
#RUN axel -a https://www.ll.mit.edu/ideval/data/1999/training/week2/monday/inside.tcpdump.gz  # Can use this instead for more pcap.  Doesn't contain resolvable DNS though, so a bad example.

# Capture.pcap gathered on AWS instance, 12-16-2016
COPY capture.pcap.gz /opt/example-capture.pcap.gz

RUN pigz -d /opt/example-capture.pcap.gz

CMD softflowd -r /opt/example-capture.pcap -n collector:50000 -v

