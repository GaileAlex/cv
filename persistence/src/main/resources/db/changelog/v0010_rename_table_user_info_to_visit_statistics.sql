--liquibase formatted sql
--changeset gaile:v0010_rename_table_user_info_to_visit_statistics.sql

ALTER TABLE user_info
    RENAME TO visit_statistics;
