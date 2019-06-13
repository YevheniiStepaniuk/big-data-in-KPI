#!/bin/bash

sleep ${WAITFORSTART:-0}

hadoop fs -mkdir /graph

hadoop fs -put /data/airlines.csv /graph/airlines.csv
hadoop fs -put /data/routes.csv /graph/routes.csv
hadoop fs -put /data/airlines-extended.csv /graph/airlines-extended.csv