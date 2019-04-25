use dm;
-- delete the data, if exists
drop table FOP_table;
-- create a table UO_table
create external table FOP_table
(
    fio string,
    address string,
    kved string,
    stan string
)
ROW FORMAT DELIMITED
FIELDS TERMINATED BY ','
STORED AS TEXTFILE
LOCATION 'hdfs://namenode:8020/tables_data/FOP/';
