FROM java:8-jdk

WORKDIR /opt
RUN wget http://apache.cs.utah.edu/storm/apache-storm-1.0.2/apache-storm-1.0.2.tar.gz
RUN tar xvzf apache-storm-1.0.2.tar.gz

RUN wget http://mirrors.ocf.berkeley.edu/apache/maven/maven-3/3.3.9/binaries/apache-maven-3.3.9-bin.tar.gz
RUN tar xvzf apache-maven-3.3.9-bin.tar.gz

RUN cd /opt/apache-storm-1.0.2/lib && wget http://central.maven.org/maven2/org/apache/storm/storm-kafka/0.9.2-incubating/storm-kafka-0.9.2-incubating.jar

ADD storm.yaml /opt/apache-storm-1.0.2/conf/storm.yaml

WORKDIR /opt/topology
COPY topology/pom.xml /opt/topology/pom.xml

COPY topology/ /opt/topology
RUN /opt/apache-maven-3.3.9/bin/mvn clean 
RUN /opt/apache-maven-3.3.9/bin/mvn package
WORKDIR /opt
