--liquibase formatted sql
--changeset gaile:v0005_create_table_user.sql

CREATE TABLE public.users
(
    id       int8         NOT NULL GENERATED ALWAYS AS IDENTITY,
    email    varchar(50)  NOT NULL,
    password varchar(120) NOT NULL,
    username varchar(20)  NOT NULL,
    role     varchar(20)  NOT NULL,
    CONSTRAINT uk6dotkott2kjsp8vw4d0m25fb7 UNIQUE (email),
    CONSTRAINT ukr43af9ap4edm43mmtq01oddj6 UNIQUE (username),
    CONSTRAINT user_pkey PRIMARY KEY (id)
);


