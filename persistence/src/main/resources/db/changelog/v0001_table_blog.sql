--liquibase formatted sql
--changeset gaile:v0001_table_blog.sql

create table blog (
    id bigint not null,
    blog_headline character varying(2000),
    blog_article int8,
    blog_image varbit,
    blog_date timestamp

);


