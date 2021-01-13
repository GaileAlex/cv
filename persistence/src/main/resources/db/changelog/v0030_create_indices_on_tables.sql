--liquibase formatted sql
--changeset gaile:v0030_create_indices_on_tables.sql

create index on db.public.comments (blog_id);

create index  on db.public.bitfinex_cryptocurrency (cryptocurrency_name);

create index  on db.public.cryptocurrency_values (bitfinex_cryptocurrency_id);

create index  on db.public.proxy_list (country, uptime);

create index  on db.public.proxy_list (speed);

create index on db.public.visit_statistic_visit_date (visit_statistics_id);

create index on db.public.visit_statistics_user_ip (visit_statistics_id);







