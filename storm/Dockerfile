FROM java:8-jdk

WORKDIR /opt
RUN wget https://archive.apache.org/dist/storm/apache-storm-1.0.2/apache-storm-1.0.2.tar.gz
RUN tar xvzf apache-storm-1.0.2.tar.gz

RUN wget https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.3.9/apache-maven-3.3.9-bin.tar.gz
RUN tar xvzf apache-maven-3.3.9-bin.tar.gz

RUN cd /opt/apache-storm-1.0.2/lib && wget https://repo1.maven.org/maven2/org/apache/storm/storm-kafka/0.9.2-incubating/storm-kafka-0.9.2-incubating.jar

ADD storm.yaml /opt/apache-storm-1.0.2/conf/storm.yaml

WORKDIR /opt/topology
COPY topology/pom.xml /opt/topology/pom.xml
RUN /opt/apache-maven-3.3.9/bin/mvn dependency:resolve  # Get dependencies first, so we don't fetch on every change.

COPY topology/ /opt/topology
RUN /opt/apache-maven-3.3.9/bin/mvn clean 
RUN /opt/apache-maven-3.3.9/bin/mvn package
WORKDIR /opt
