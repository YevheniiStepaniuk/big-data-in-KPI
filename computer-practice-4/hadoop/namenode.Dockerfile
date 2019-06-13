FROM bde2020/hadoop-namenode:1.1.0-hadoop2.8-java8

COPY ./bootstrap.sh /bootstrap.sh

COPY ./airlines.csv /data/airlines.csv
COPY ./routes.csv /data/routes.csv
COPY ./airports-extended.csv /data/airlines-extended.csv