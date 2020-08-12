--liquibase formatted sql
--changeset gaile:v0012_create_table_visit_statistic_user.sql

CREATE TABLE public.visit_statistic_user
(
    id                  int8 NOT NULL GENERATED ALWAYS AS IDENTITY,
    visit_date          date NOT NULL,
    visit_statistics_id int8 NOT NULL,
    CONSTRAINT visit_statistic_user_pk PRIMARY KEY (id)
);

ALTER TABLE visit_statistic_user
    ADD CONSTRAINT visit_statistic_user_fk FOREIGN KEY (visit_statistics_id) REFERENCES visit_statistics (id) ON DELETE CASCADE;
