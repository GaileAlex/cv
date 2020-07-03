--liquibase formatted sql
--changeset gaile:v0002_create_table_books.sql

create table books (
    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY,
    book_title character varying(200),
    book_article character varying(200),
    book_date timestamp,
    book_text character varying(2000)

);


