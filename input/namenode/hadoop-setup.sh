hadoop fs -mkdir /tables_data;
hadoop fs -mkdir /tables_data/UO;
hadoop fs -mkdir /tables_data/FOP;
# hadoop fs -put ./15.1-EX_XML_EDR_UO.xml /tables_data/UO/
# hadoop fs -put ./15.2-EX_XML_EDR_FOP.xml /tables_data/FOP/
hadoop fs -put ./UO.csv /tables_data/UO/;
hadoop fs -put ./FOP.csv /tables_data/FOP/;