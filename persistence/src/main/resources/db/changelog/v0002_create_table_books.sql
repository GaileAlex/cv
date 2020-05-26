--liquibase formatted sql
--changeset gaile:v0002_create_table_books.sql

create table books (
    id bigint not null,
    book_title character varying(200),
    book_article character varying(200),
    book_date timestamp,
    book_text character varying(2000)

);

CREATE SEQUENCE books_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE books_id_seq OWNED BY books.id;

ALTER TABLE ONLY books ALTER COLUMN id SET DEFAULT nextval('books_id_seq'::regclass);
