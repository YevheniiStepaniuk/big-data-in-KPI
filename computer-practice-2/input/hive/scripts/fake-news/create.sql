use bd;

drop table fake_news_table;

create external table fake_news_table 
(
    title string,
    description string,
    type string,
    language string,
    author_info struct
    <
        first_name: string,
        last_name: string,
        title: string
    >,
    country string,
    category string,
    added_date date,
    priority int,
    logo_info struct
    <
        link: string,
        alt: string
    >
)
partitioned by (year int, month string)
clustered by (type, category, country) into 2 buckets
ROW FORMAT serde 'org.openx.data.jsonserde.JsonSerDe'
LOCATION 'hdfs://namenode:8020/tables_data/fake_news/';