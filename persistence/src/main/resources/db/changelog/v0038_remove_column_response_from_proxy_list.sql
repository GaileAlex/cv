--liquibase formatted sql
--changeset gaile:v0038_remove_column_response_from_proxy_list.sql

ALTER TABLE public.proxy_list
DROP
COLUMN response;



