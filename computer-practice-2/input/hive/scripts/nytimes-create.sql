use bd;

drop table ny_times_table;

create external table ny_times_table 
(
    web_url string,
    snippet string,
    lead_paragraph string,
    abstract string,
    print_page string,
    source string,
    document_type string,
    headline_main string,
    headline_kicker string,
    headline_print_headline string,
    headline_content_kicker string,

    -- multimedia array
    -- <
    --     struct
    --     <
    --         width: SMALLINT, 
    --         url: string, 
    --         height: SMALLINT, 
    --         subtype: string, 
    --         legacy: struct
    --         <
    --             xlargewidth: string, 
    --             xlarge: string, 
    --             xlargeheight: string
    --         >,
    --         type: string
    --     >
    -- >,
    -- headline struct
    -- <
    --     seo: string,
    --     main: string,
    --     content_kicker: string,
    --     kicker: string,
    --     print_headline: string
    -- >,
    -- keywords array
    -- <
    --     struct
    --     <
    --         rank: string,
    --         is_major: string,
    --         name: string,
    --         value: string
    --     >
    -- >,
    pub_date date,
    -- document_type string,
    news_desk string,
    section_name string,
    subsection_name string,
    -- byline struct
    -- <
    --     original: string,
    --     person: array
    --     <
    --         struct
    --         <
    --             organization: string,
    --             role: string,
    --             rank: int,
    --             firstname: string,
    --             lastname: string
    --         >
    --     >
    -- >,
    type_of_material string,
    word_count INT,
    slideshow_credits string
)
partitioned by (dt string)
clustered by (source, section_name, subsection_name) into 2 buckets
-- ROW FORMAT serde 'org.apache.hadoop.hive.contrib.serde2.JsonSerde'
-- WITH SERDEPROPERTIES ( "ignore.malformed.json" = "true")
ROW FORMAT DELIMITED
FIELDS TERMINATED BY ','
STORED AS TEXTFILE
LOCATION 'hdfs://namenode:8020/tables_data/ny_times/';