FROM java:8-jdk

WORKDIR /opt

RUN wget http://apache.osuosl.org/kafka/0.8.1.1/kafka_2.10-0.8.1.1.tgz
RUN tar xvzf kafka_2.10-0.8.1.1.tgz
ADD config /opt/kafka_2.10-0.8.1.1/config
CMD /bin/bash -c "sleep 5 && /opt/kafka_2.10-0.8.1.1/bin/kafka-server-start.sh /opt/kafka_2.10-0.8.1.1/config/server.properties"