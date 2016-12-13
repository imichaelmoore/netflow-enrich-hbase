FROM java:8-jdk

WORKDIR /opt

RUN wget http://ftp.wayne.edu/apache/hbase/stable/hbase-1.2.4-bin.tar.gz
RUN tar xvzf hbase-1.2.4-bin.tar.gz

ADD hbase-site.xml /opt/hbase-1.2.4/conf/hbase-site.xml 
ADD zoo.cfg /opt/hbase-1.2.4/conf/zoo.cfg

CMD /opt/hbase-1.2.4/bin/hbase master start


#RUN wget https://dl.dropboxusercontent.com/u/730827/hue/releases/3.11.0/hue-3.11.0.tgz
#RUN tar xvzf /opt/hue-3.11.0.tgz
#RUN cd /opt/hue-3.11.0 && make apps

#ENV JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64/jre/
#ADD hue.ini /opt/hue-3.11.0/desktop/conf/hue.ini
#ADD go.sh /opt/go.sh
#CMD /bin/bash /opt/go.sh

