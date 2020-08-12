--liquibase formatted sql
--changeset gaile:v0013_create_trigger_visit_statistics.sql

create or replace function visit_statistics_trigger_function()
    RETURNS TRIGGER language plpgsql
as '
    BEGIN
    INSERT INTO visit_statistic_user (visit_date, visit_statistics_id)
    values (current_timestamp, new.id); RETURN NEW; end; ';


CREATE TRIGGER visit_statistics_trigger
    AFTER INSERT OR UPDATE
    ON visit_statistics
    FOR EACH ROW
EXECUTE PROCEDURE visit_statistics_trigger_function()


