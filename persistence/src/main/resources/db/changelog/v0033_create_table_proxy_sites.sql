--liquibase formatted sql
--changeset gaile:v0033_create_table_proxy_sites.sql

CREATE TABLE public.proxy_sites
(
    id  bigint       NOT NULL GENERATED ALWAYS AS IDENTITY,
    url varchar(500) NOT NULL
);

create index on db.public.proxy_sites (url);




