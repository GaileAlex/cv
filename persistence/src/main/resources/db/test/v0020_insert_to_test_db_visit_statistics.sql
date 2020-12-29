--liquibase formatted sql
--changeset gaile:v0020_insert_to_test_db_visit_statistics.sql  context:test

INSERT INTO public.visit_statistics (user_location, first_visit,
                                               last_visit, total_visits, user_name, user_city)
values ('Estonia', '2020-09-07 08:39:14', '2020-09-07 17:59:09', 5, 'Admin', 'Tallinn');

INSERT INTO public.visit_statistic_user (visit_date, visit_statistics_id)
values ('2020-09-07 08:39:14', 1);

INSERT INTO public.visit_statistics_user_ip (user_ip, visit_statistics_id)
values ('111.111.111.111', 1);


