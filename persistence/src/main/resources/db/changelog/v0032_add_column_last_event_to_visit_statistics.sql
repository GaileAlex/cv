--liquibase formatted sql
--changeset gaile:v0032_add_column_last_event_to_visit_statistics.sql

ALTER TABLE public.visit_statistics
    ADD COLUMN last_event timestamp without time zone;



