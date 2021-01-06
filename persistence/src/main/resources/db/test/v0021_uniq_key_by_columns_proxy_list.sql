--liquibase formatted sql
--changeset gaile:v0021_uniq_key_by_columns_proxy_list.sql

ALTER TABLE public.proxy_list
    ADD unique (ip_address, port);



