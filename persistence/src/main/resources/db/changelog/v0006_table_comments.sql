--liquibase formatted sql
--changeset gaile:v0006_table_comments.sql

CREATE TABLE comments
(
    id      int8         NOT NULL GENERATED ALWAYS AS IDENTITY,
    comment varchar(500) NOT NULL,
    date    timestamp    NOT NULL  DEFAULT NOW(),
    blog_id int8         NOT NULL,
    user_id int8         NOT NULL,
    CONSTRAINT comments_pk PRIMARY KEY (id, blog_id, user_id)
);

ALTER TABLE public."comments"
    ADD CONSTRAINT blog_fk FOREIGN KEY (blog_id) REFERENCES blog (id) ON DELETE CASCADE;
ALTER TABLE public."comments"
    ADD CONSTRAINT user_fk FOREIGN KEY (user_id) REFERENCES users (id);
