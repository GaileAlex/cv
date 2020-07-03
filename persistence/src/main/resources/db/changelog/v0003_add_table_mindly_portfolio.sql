--liquibase formatted sql
--changeset gaile:v0003_add_table_mindly_portfolio

CREATE TABLE mindly
(
    id                      bigint       NOT NULL GENERATED ALWAYS AS IDENTITY,
    mindly_crypto_currency  varchar(200) NULL,
    mindly_amount           bigint       NULL,
    mindly_date_of_purchase timestamp without time zone,
    mindly_wallet_location  varchar(200) NULL
);
