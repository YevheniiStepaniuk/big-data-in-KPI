# How to use

## To start a HDFS

```
    docker-compose -f ./docker-compose.hadoop.yml up -d
```

## To start a Spark

```
    docker-compose -f ./docker-compose.spark.yml up -d spark-worker
```

## To start a Cassandra

```bash
    //because of windows issue we need two compose files

    docker-compose -f ./docker-compose.cassandra-seed.yml up -d
    docker-compose -f ./docker-compose.cassandra.yml up -d
```

- create keyspace and table in cassandra (/cassandra/setup.sql)

## To put data set into hdfs

```
    upload file via hue (localhost:8088) or manual inside container
```

## To start application

- compile scala app, using sbt or idea
- docker-compose -f .\docker-compose.spark.yml up --build spark-streaming-rdd

# Helpers

Generate struct type from csv header

```javascript
"csv header"
  .split(",")
  .map(s => `add("${s}", "${s.includes("Date") ? "date" : "string"}")`)
  .join("\n.");
```
