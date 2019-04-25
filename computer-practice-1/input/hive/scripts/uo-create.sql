use dm;
-- delete the data, if exists
drop table UO_table;
-- create a table UO_table
create external table UO_table
(
    name string,
    edrpou string,
    address string,
    boss string,
    founders string,
    fio string,
    kved string,
    stan string
)
ROW FORMAT DELIMITED
FIELDS TERMINATED BY ','
STORED AS TEXTFILE
LOCATION 'hdfs://namenode:8020/tables_data/UO/';
