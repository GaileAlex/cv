--liquibase formatted sql
--changeset gaile:v0025_create_table_visit_statistic_events.sql

CREATE TABLE public.visit_statistic_events
(
    id                  bigint      NOT NULL GENERATED ALWAYS AS IDENTITY,
    events              varchar(50) NOT NULL,
    event_date          timestamp without time zone,
    visit_statistics_id int8        NOT NULL,
    CONSTRAINT visit_statistic_events_pk PRIMARY KEY (id)
);

ALTER TABLE visit_statistic_events
    ADD CONSTRAINT visit_statistic_events_fk FOREIGN KEY (visit_statistics_id)
        REFERENCES visit_statistics (id) ON DELETE CASCADE;

create index on public.visit_statistic_events (visit_statistics_id);




