--liquibase formatted sql
--changeset gaile:v0032_add_column_last_event_to_visit_statistics.sql

ALTER TABLE public.proxy_list
    ADD COLUMN last_successful_check timestamp without time zone;



