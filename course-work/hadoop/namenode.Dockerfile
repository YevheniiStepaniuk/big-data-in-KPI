FROM bde2020/hadoop-namenode:1.1.0-hadoop2.8-java8

COPY ./bootstrap.sh /bootstrap.sh

COPY ./howpop_train.csv .
