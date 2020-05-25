--liquibase formatted sql
--changeset gaile:v0001_table_book

create table book (
    id bigint not null,
    book_title character varying(200),
    book_article character varying(200),
    book_date timestamp,
    book_text character varying(2000)

);


