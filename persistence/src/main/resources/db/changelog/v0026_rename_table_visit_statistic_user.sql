--liquibase formatted sql
--changeset gaile:v0026_rename_table_visit_statistic_user.sql

DROP TRIGGER if exists visit_statistics_trigger on visit_statistics;

ALTER TABLE if exists public.visit_statistic_user
    RENAME TO visit_statistic_visit_date;



