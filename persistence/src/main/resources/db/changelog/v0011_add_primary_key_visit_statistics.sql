--liquibase formatted sql
--changeset gaile:v0011_add_primary_key_visit_statistics.sql

ALTER TABLE public.visit_statistics
    ADD CONSTRAINT visit_statistics_pk PRIMARY KEY (id);

