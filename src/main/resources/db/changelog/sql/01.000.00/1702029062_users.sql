--liquibase formatted sql

--changeset 1702029062_users.sql:1
CREATE TABLE IF NOT EXISTS _users
(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name character varying(255) NOT NULL,
    email character varying(255) NOT NULL UNIQUE,
    password character varying(255) NOT NULL,
    role character varying(255) NOT NULL,
    create_date timestamp without time zone,
    update_date timestamp without time zone
);

CREATE INDEX IF NOT EXISTS index_email ON _users(email);
