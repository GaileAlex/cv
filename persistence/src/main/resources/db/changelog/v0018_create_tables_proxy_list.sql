--liquibase formatted sql
--changeset gaile:v0018_create_tables_proxy_list.sql

CREATE TABLE public.proxy_list
(
    id            bigint      NOT NULL GENERATED ALWAYS AS IDENTITY,
    ip_address    varchar(20) NOT NULL,
    port          int8        NOT NULL,
    protocol      varchar(20),
    country       varchar(50),
    anonymity     varchar(20),
    speed         int8,
    uptime        numeric,
    response      int8,
    last_checked  timestamp without time zone,
    number_checks int8
);




