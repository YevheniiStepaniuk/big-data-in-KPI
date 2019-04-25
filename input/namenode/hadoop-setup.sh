hadoop fs -mkdir /tables_data;
hadoop fs -mkdir /tables_data/UO;
hadoop fs -mkdir /tables_data/FOP;
hadoop fs -put ./UO.csv /tables_data/UO/;
hadoop fs -put ./FOP.csv /tables_data/FOP/;