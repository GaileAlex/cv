--liquibase formatted sql
--changeset gaile:v0014_create_tables_bitfinex_cryptocurrency_cryptocurrency_values.sql

CREATE TABLE public.bitfinex_cryptocurrency
(
    id                  int8        NOT NULL GENERATED ALWAYS AS IDENTITY,
    cryptocurrency_name varchar(20) NOT NULL,
    CONSTRAINT bitfinex_cryptocurrency_pk PRIMARY KEY (id)
);

CREATE TABLE public.cryptocurrency_values
(
    id                         int8                        NOT NULL GENERATED ALWAYS AS IDENTITY,
    date_cryptocurrency        timestamp without time zone NOT NULL,
    value_cryptocurrency       numeric(20, 10)             NOT NULL,
    bitfinex_cryptocurrency_id int8                        NOT NULL,
    CONSTRAINT cryptocurrency_values_pk PRIMARY KEY (id),
    CONSTRAINT cryptocurrency_values_fk FOREIGN KEY (bitfinex_cryptocurrency_id) REFERENCES
        public.bitfinex_cryptocurrency (id) ON DELETE CASCADE ON UPDATE CASCADE
);

INSERT INTO bitfinex_cryptocurrency (cryptocurrency_name)
values ('Ethereum');
INSERT INTO bitfinex_cryptocurrency (cryptocurrency_name)
values ('Ripple');
INSERT INTO bitfinex_cryptocurrency (cryptocurrency_name)
values ('Bitcoin');


