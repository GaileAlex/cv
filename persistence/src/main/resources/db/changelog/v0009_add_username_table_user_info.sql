--liquibase formatted sql
--changeset gaile:v0009_add_username_table_user_info.sql

ALTER TABLE public.user_info
    ADD COLUMN user_name varchar(20);



