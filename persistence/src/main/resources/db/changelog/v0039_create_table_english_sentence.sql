--liquibase formatted sql
--changeset gaile:v0039_create_table_english_sentence.sql

CREATE TABLE public.english_sentences
(
    id                 bigint        NOT NULL GENERATED ALWAYS AS IDENTITY,
    sentence           varchar(5500) NOT NULL,
    sentence_translate varchar(5500)
);





