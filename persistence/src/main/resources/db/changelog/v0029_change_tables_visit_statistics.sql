--liquibase formatted sql
--changeset gaile:v0029_change_tables_visit_statistics.sql

ALTER TABLE public.proxy_list
    ALTER COLUMN  country type varchar(100);





