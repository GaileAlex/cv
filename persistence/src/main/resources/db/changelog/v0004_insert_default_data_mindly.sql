--liquibase formatted sql
--changeset gaile:v0004_insert_default_data_mindly

INSERT INTO mindly (mindly_crypto_currency, mindly_amount, mindly_date_of_purchase, mindly_wallet_location) values ( 'Bitcoin', 1, '2020-01-05', 'My personal wallet');
INSERT INTO mindly (mindly_crypto_currency, mindly_amount, mindly_date_of_purchase, mindly_wallet_location) values ( 'Ethereum', 1, '2020-01-05', 'My personal wallet');
INSERT INTO mindly (mindly_crypto_currency, mindly_amount, mindly_date_of_purchase, mindly_wallet_location) values ( 'Ripple', 1, '2020-01-05', 'My personal wallet');


