FROM java:8-jdk

WORKDIR /opt

RUN wget http://ftp.wayne.edu/apache/hbase/stable/hbase-1.2.4-bin.tar.gz
RUN tar xvzf hbase-1.2.4-bin.tar.gz

ADD hbase-site.xml /opt/hbase-1.2.4/conf/hbase-site.xml 
ADD zoo.cfg /opt/hbase-1.2.4/conf/zoo.cfg

CMD /opt/hbase-1.2.4/bin/hbase master start