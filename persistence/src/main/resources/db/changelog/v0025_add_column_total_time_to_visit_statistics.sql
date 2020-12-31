--liquibase formatted sql
--changeset gaile:v0025_add_column_total_time_to_visit_statistics.sql

ALTER TABLE public.visit_statistics
    ADD COLUMN total_time_on_site bigint;



