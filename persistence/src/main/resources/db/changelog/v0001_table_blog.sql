--liquibase formatted sql
--changeset gaile:v0001_table_blog.sql

CREATE TABLE public.blog
(
    id            int8                        NOT NULL GENERATED ALWAYS AS IDENTITY,
    blog_headline varchar(2000)               NOT NULL,
    blog_article  int8                        NOT NULL,
    blog_image    varbit                      NOT NULL,
    blog_date     timestamp without time zone NOT NULL DEFAULT NOW(),
    CONSTRAINT blog_pkey PRIMARY KEY (id)
);


