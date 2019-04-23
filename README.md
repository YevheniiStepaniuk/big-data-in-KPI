# How to use HDFS/Spark Workbench

To start an HDFS/Spark Workbench:

```
    docker-compose up -d
```

To scale up spark-workers:

```
    docker-compose up -d --scale datanode=3
```

## Starting workbench with Hive support

Before starting the next command, check that the previous service is running correctly (with docker logs servicename).

```
docker-compose -f docker-compose-hive.yml up -d namenode hive-metastore-postgresql
docker-compose -f docker-compose-hive.yml up -d datanode hive-metastore
docker-compose -f docker-compose-hive.yml up -d hive-server

docker-compose -f .\docker-compose.hive.yml -f .\docker-compose.hadoop.yml up -d
docker-compose exec namenode bash
cd hadoop/dfs/input-data/

docker-compose exec hive-server bash
```

## Interfaces

- Namenode: http://localhost:50070
- Datanode: http://localhost:50075
- Hue (HDFS Filebrowser): http://localhost:8088

## Important

When opening Hue, you might encounter `NoReverseMatch: u'about' is not a registered namespace` error after login. I disabled 'about' page (which is default one), because it caused docker container to hang. To access Hue when you have such an error, you need to append /home to your URI: `http://docker-host-ip:8088/home`
