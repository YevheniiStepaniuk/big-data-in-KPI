#!/bin/bash

docker-compose -f ./docker-compose.hadoop.yml up -d --build namenode

docker-compose -f ./docker-compose.hadoop.yml scale datanode=3

docker-compose -f ./docker-compose.hadoop.yml exec namenode sh -c /bootstrap.sh

 docker-compose -f ./docker-compose.hadoop.yml up -d hue