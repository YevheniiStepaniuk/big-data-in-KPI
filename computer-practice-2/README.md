# How to use

To start a HDFS

```
    docker-compose -f ./docker-compose.hadoop.yml up -d
```

To start a Hive

```
    docker-compose -f ./docker-compose.hive.yml up -d
```

To start a ETL

```
    docker-compose -f ./docker-compose.etl.yml up -d
```

## How to seed data to Hadoop

```
docker-compose -f docker-compose.hadoop.yml exec namenode bash
cd hadoop/dfs/input-data/

hadoop fs -mkdir /tables_data;
hadoop fs -mkdir /tables_data/ny_times;
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
- turn off safe mode `hdfs dfsadmin -safemode leave`
- update table partitions `hive -e 'msck repair table {table name}`
