--liquibase formatted sql
--changeset gaile:v0008_change_table_user_info_table.sql

ALTER TABLE public.user_info
    ALTER COLUMN last_visit set NOT NULL;

ALTER TABLE public.user_info
    DROP COLUMN total_time_on_site;

ALTER TABLE public.user_info
    DROP COLUMN time_on_site_last_visit;



