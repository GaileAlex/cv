--liquibase formatted sql
--changeset gaile:v0021_insert_to_crypto_values.sql  context:test

INSERT INTO public.cryptocurrency_values (date_cryptocurrency, value_cryptocurrency, bitfinex_cryptocurrency_id)
values ('2020-12-10 08:39:14', 5, 1);

INSERT INTO public.cryptocurrency_values (date_cryptocurrency, value_cryptocurrency, bitfinex_cryptocurrency_id)
values ('2020-12-10 08:39:14', 10, 2);

INSERT INTO public.cryptocurrency_values (date_cryptocurrency, value_cryptocurrency, bitfinex_cryptocurrency_id)
values ('2020-12-10 08:39:14', 15, 3);


