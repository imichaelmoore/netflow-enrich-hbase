FROM ubuntu:xenial

RUN apt-get update
RUN apt-get -y install softflowd gzip axel

WORKDIR /opt
RUN axel -a https://www.ll.mit.edu/ideval/data/1999/training/week2/monday/inside.tcpdump.gz
RUN apt-get -y install pigz
RUN pigz -d /opt/inside.tcpdump.gz

CMD softflowd -r /opt/inside.tcpdump -n collector:50000 -v