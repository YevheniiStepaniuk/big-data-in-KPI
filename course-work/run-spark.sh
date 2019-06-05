#!/bin/bash

docker-compose -f ./docker-compose.spark.yml up -d spark-master

docker-compose -f ./docker-compose.spark.yml up -d spark-worker

docker-compose -f ./docker-compose.spark.yml scale spark-worker=3

docker-compose -f ./docker-compose.spark.yml up --build course-app