use bd;

alter table ny_times_table add partition (dt='2012-01') location 'hdfs://namenode:8020/tables_data/ny_times/dt=2012-01'