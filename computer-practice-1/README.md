# How to use

To start a HDFS

```
    docker-compose -f ./docker-compose.hadoop.yml up -d
```

To start a Hive

```
    docker-compose -f ./docker-compose.hive.yml up -d
```

To start both

```
    docker-compose -f .\docker-compose.hadoop.yml -f .\docker-compose.hive.yml up -d

```

## How to seed data to Hadoop

Data can be taken from cource gdrive

```
docker-compose -f docker-compose.hadoop.yml exec namenode bash
cd hadoop/dfs/input-data/

hadoop fs -mkdir /tables_data;
hadoop fs -mkdir /tables_data/UO;
hadoop fs -mkdir /tables_data/FOP;
hadoop fs -put ./UO.csv /tables_data/UO/;
hadoop fs -put ./FOP.csv /tables_data/FOP/;
```

## How to run wordcount example

```
hadoop jar /opt/hadoop-2.8.0/share/hadoop/mapreduce/hadoop-mapreduce-examples-2.8.0.jar wordcount /tables_data/UO/ output
```

## How to run scripts in the hive container

```
docker-compose -f docker-compose.hive.yml exec hive-server bash 
cd hive/data/scripts
hive -f {script-name}
```

## Interfaces

- Namenode: http://localhost:50070
- Datanode: http://localhost:50075

## Useful commands

- get namenode url: `hdfs getconf -confKey fs.default.name`