--liquibase formatted sql
--changeset gaile:v0001_create_table_blog.sql

CREATE TABLE public.blog
(
    id            int8                        NOT NULL GENERATED ALWAYS AS IDENTITY,
    blog_headline varchar(200)                NOT NULL,
    blog_article  varchar(200000)             NOT NULL,
    blog_image    bytea,
    blog_date     timestamp without time zone NOT NULL DEFAULT NOW(),
    CONSTRAINT blog_pkey PRIMARY KEY (id)
);


