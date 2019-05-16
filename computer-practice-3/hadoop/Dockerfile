FROM bde2020/hadoop-namenode:2.0.0-hadoop3.1.1-java8 as hadoop
WORKDIR /hadoop/dfs/dataset/

COPY ${DATASET_FILE} ./data_set.csv
COPY hadoop_setup.sh .
