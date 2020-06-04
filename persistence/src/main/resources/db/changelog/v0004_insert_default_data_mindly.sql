--liquibase formatted sql
--changeset gaile:v0004_insert_default_data_mindly

INSERT INTO mindly (mindly_crypto_currency, mindly_amount, mindly_date_of_purchase, mindly_wallet_location) values ( 'Bitcoin', 33, '2020-01-05', '100');


