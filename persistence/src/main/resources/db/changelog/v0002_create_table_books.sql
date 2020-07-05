--liquibase formatted sql
--changeset gaile:v0002_create_table_books.sql

CREATE TABLE public.books
(
    id           int8                        NOT NULL GENERATED ALWAYS AS IDENTITY,
    book_title   varchar(200)                NOT NULL,
    book_article varchar(200)                NOT NULL,
    book_date    timestamp without time zone NOT NULL,
    book_text    varchar(2000)               NOT NULL
);


