--liquibase formatted sql
--changeset gaile:v0003_add_table_mindly_portfolio

create table mindly (
    id bigint not null,
    mindly_crypto_currency character varying(200),
    mindly_amount int8,
    mindly_date_of_purchase timestamp without time zone,
    mindly_wallet_location character varying(200)

);

CREATE SEQUENCE mindly_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE mindly_id_seq OWNED BY mindly.id;

ALTER TABLE ONLY mindly ALTER COLUMN id SET DEFAULT nextval('books_id_seq'::regclass);
