--liquibase formatted sql
--changeset gaile:v0027_change_tables_visit_statistics.sql

ALTER TABLE public.visit_statistics
    ALTER COLUMN last_visit DROP NOT NULL;

ALTER TABLE public.visit_statistics
    ADD COLUMN session_id varchar(500);

ALTER TABLE public.visit_statistics_user_ip
    ALTER COLUMN user_ip DROP NOT NULL;




