# How to use HDFS/Spark Workbench

To start an HDFS

```
    docker-compose -f ./docker-compose.hadoop.yml up -d
```

To start an Hive

```
    docker-compose -f ./docker-compose.hive.yml up -d
```

To start both

```
    docker-compose -f .\docker-compose.hadoop.yml -f .\docker-compose.hive.yml up -d

```

## How to seed data to Hadoop

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

## How to run scripts in the hive

```
docker-compose -f docker-compose-hive.yml up -d hive-server
cd hive/data/scripts
hive -f {script-name}
```

## Interfaces

- Namenode: http://localhost:50070
- Datanode: http://localhost:50075
- Hue (HDFS Filebrowser): http://localhost:8088/home
- Application History: http://localhost:80188/applicationhistory
- Nodemanager: http://localhost:80042/node
- Resource manager: http://localhost:80088/

## Important

When opening Hue, you might encounter `NoReverseMatch: u'about' is not a registered namespace` error after login. I disabled 'about' page (which is default one), because it caused docker container to hang. To access Hue when you have such an error, you need to append /home to your URI: `http://docker-host-ip:8088/home`
