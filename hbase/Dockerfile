FROM java:8-jdk

WORKDIR /opt

RUN wget https://archive.apache.org/dist/hbase/1.2.4/hbase-1.2.4-bin.tar.gz
RUN tar xvzf hbase-1.2.4-bin.tar.gz

ADD hbase-site.xml /opt/hbase-1.2.4/conf/hbase-site.xml 
ADD zoo.cfg /opt/hbase-1.2.4/conf/zoo.cfg

CMD /opt/hbase-1.2.4/bin/hbase master start
