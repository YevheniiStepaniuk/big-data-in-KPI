sleep ${WAITFORSTART:-0}

hadoop fs -mkdir /tables_data

hadoop fs -put /howpop_train.csv /tables_data/dataset.csv