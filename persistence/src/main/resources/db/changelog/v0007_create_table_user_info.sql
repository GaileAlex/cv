--liquibase formatted sql
--changeset gaile:v0007_create_table_user_info.sql

CREATE SEQUENCE total_visits_seq;

CREATE TABLE public.user_info
(
    id                      int8                        NOT NULL GENERATED ALWAYS AS IDENTITY,
    user_ip                 varchar(20)                 NULL,
    user_location           varchar(40)                 NULL,
    first_visit             timestamp without time zone NULL,
    last_visit              timestamp without time zone NULL,
    total_visits            int8                        NULL,
    total_time_on_site      date                        NULL,
    time_on_site_last_visit date                        NULL
);


ALTER SEQUENCE total_visits_seq
    OWNED BY user_info.total_visits;
