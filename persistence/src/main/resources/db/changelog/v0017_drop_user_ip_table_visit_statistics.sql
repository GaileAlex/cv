--liquibase formatted sql
--changeset gaile:v0017_drop_user_ip_table_visit_statistics.sql.sql

ALTER TABLE public.visit_statistics
    DROP COLUMN user_ip;



