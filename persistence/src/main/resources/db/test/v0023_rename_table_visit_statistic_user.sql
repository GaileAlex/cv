--liquibase formatted sql
--changeset gaile:v0023_rename_table_visit_statistic_user.sql

DROP TRIGGER visit_statistics_trigger on visit_statistics;

ALTER TABLE public.visit_statistic_user
    RENAME TO visit_statistic_visit_date;



