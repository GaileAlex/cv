--liquibase formatted sql
--changeset gaile:v0007_create_table_user_info.sql

CREATE TABLE public.user_info
(
    id                      int8    NOT NULL GENERATED ALWAYS AS IDENTITY,
    user_ip                 varchar NULL,
    user_location           varchar NULL,
    first_visit             date    NULL,
    last_visit              date    NULL,
    total_visits            int8    NULL,
    total_time_on_site      date    NULL,
    time_on_site_last_visit date    NULL
);



