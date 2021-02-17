--liquibase formatted sql
--changeset gaile:v0034_uniq_key_by_columns_proxy_sites.sql

ALTER TABLE public.proxy_sites
    ADD unique (url);



