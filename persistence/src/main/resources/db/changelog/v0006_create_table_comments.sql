--liquibase formatted sql
--changeset gaile:v0006_create_table_comments.sql

CREATE TABLE comments
(
    id        int8         NOT NULL GENERATED ALWAYS AS IDENTITY,
    comment   varchar(500) NOT NULL,
    date      timestamp    NOT NULL DEFAULT NOW(),
    blog_id   int8         NOT NULL,
    user_name varchar(20)  NOT NULL,
    CONSTRAINT comments_pk PRIMARY KEY (id)
);

ALTER TABLE comments
    ADD CONSTRAINT blog_fk FOREIGN KEY (blog_id) REFERENCES blog (id) ON DELETE CASCADE;

