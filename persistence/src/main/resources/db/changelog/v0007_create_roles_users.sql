--liquibase formatted sql
--changeset gaile:v0007_create_roles_users.sql

CREATE TABLE public.roles (
                              id bigint NOT NULL GENERATED ALWAYS AS IDENTITY,
                              "name" varchar(20) NULL,
                              CONSTRAINT roles_pkey PRIMARY KEY (id)
);


CREATE TABLE public.users (
                              id bigint NOT NULL GENERATED ALWAYS AS IDENTITY,
                              email varchar(50) NULL,
                              password varchar(120) NULL,
                              username varchar(20) NULL,
                              CONSTRAINT uk6dotkott2kjsp8vw4d0m25fb7 UNIQUE (email),
                              CONSTRAINT ukr43af9ap4edm43mmtq01oddj6 UNIQUE (username),
                              CONSTRAINT users_pkey PRIMARY KEY (id)
);

CREATE TABLE public.user_roles (
                                   user_id int8 NOT NULL,
                                   role_id int4 NOT NULL,
                                   CONSTRAINT user_roles_pkey PRIMARY KEY (user_id, role_id),
                                   CONSTRAINT fkh8ciramu9cc9q3qcqiv4ue8a6 FOREIGN KEY (role_id) REFERENCES roles(id),
                                   CONSTRAINT fkhfh9dx7w3ubf1co1vdev94g3f FOREIGN KEY (user_id) REFERENCES users(id)
);
