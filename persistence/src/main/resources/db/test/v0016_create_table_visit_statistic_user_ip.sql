--liquibase formatted sql
--changeset gaile:v0016_create_table_visit_statistic_user_ip.sql

CREATE TABLE public.visit_statistics_user_ip
(
    id                  int8        NOT NULL GENERATED ALWAYS AS IDENTITY,
    user_ip             varchar(50) NOT NULL,
    visit_statistics_id int8        NOT NULL,
    CONSTRAINT visit_statistics_user_ip_pk PRIMARY KEY (id)
);

ALTER TABLE visit_statistics_user_ip
    ADD CONSTRAINT visit_statistics_user_ip_fk FOREIGN KEY (visit_statistics_id)
        REFERENCES visit_statistics (id) ON DELETE CASCADE;

insert into visit_statistics_user_ip (user_ip, visit_statistics_id)
select user_ip, id
from visit_statistics;


