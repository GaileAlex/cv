--liquibase formatted sql
--changeset gaile:v0015_add_column_city_to_table_visit_statistics.sql

ALTER TABLE public.visit_statistics
    ADD COLUMN user_city varchar(100);



