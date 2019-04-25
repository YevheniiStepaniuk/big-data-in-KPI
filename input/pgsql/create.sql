create table FOP_table
(
    id serial NOT NULL PRIMARY KEY,
    fio text,
    address text,
    kved text,
    stan text
);

create table UO_table
(
    id serial NOT NULL PRIMARY KEY,
    name text,
    edrpou text,
    address text,
    boss text,
    founders text,
    fio text,
    kved text,
    stan text
);
