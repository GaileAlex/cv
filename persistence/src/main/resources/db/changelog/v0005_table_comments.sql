--liquibase formatted sql
--changeset gaile:v0005_table_comments.sql

create table comments (
    id bigint not null,
    blog_comment character varying(500),
    blog_date timestamp,
    blog_username character varying(20),
    blog_pkey bigint
);

ALTER TABLE ONLY blog
    ADD CONSTRAINT blog_pkey PRIMARY KEY (id);


ALTER TABLE ONLY comments
    ADD CONSTRAINT fk9ygg35fyealdnh354ntcn7c9o FOREIGN KEY (blog_pkey) REFERENCES blog(id);
