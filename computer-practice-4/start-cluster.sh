#!/bin/bash

docker-compose -f ./docker-compose.hadoop.yml up -d --build namenode

docker-compose -f ./docker-compose.hadoop.yml scale datanode=1

docker-compose -f ./docker-compose.hadoop.yml up -d hue

docker-compose -f ./docker-compose.spark.yml up -d spark-master

docker-compose -f ./docker-compose.spark.yml scale spark-worker=3 

docker-compose -f ./docker-compose.hadoop.yml exec namenode sh -c /bootstrap.sh
