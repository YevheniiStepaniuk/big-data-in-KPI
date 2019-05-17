# How to use

To start a HDFS

```
    docker-compose -f ./docker-compose.hadoop.yml up -d
```

To put data set into hdfs

```
    docker-compose -f ./docker-compose.hadoop.yml exec namenode bash
    /hadoop/dfs/dataset/hadoop_setup.sh
```