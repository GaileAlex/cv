--liquibase formatted sql
--changeset gaile:v0040_create_table_chat_message.sql

CREATE TABLE public.chat_message
(
    id         bigint      NOT NULL GENERATED ALWAYS AS IDENTITY,
    user_name varchar(50) NOT NULL,
    role       varchar(20) NOT NULL,
    content    text,
    created_at timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP
);





