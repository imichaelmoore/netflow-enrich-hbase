FROM java:8-jdk

WORKDIR /opt

RUN wget http://mirror.nexcess.net/apache/kafka/0.10.1.0/kafka_2.11-0.10.1.0.tgz
RUN tar xvzf kafka_2.11-0.10.1.0.tgz
ADD config /opt/kafka_2.11-0.10.1.0/config
CMD /opt/kafka_2.11-0.10.1.0/bin/kafka-server-start.sh /opt/kafka_2.11-0.10.1.0/config/server.properties