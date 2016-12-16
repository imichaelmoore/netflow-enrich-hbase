FROM python:2.7

COPY netflow-collector.py /opt

RUN pip install --upgrade kafka-python

CMD /usr/bin/python /opt/netflow-collector.py