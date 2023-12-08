--liquibase formatted sql

--changeset 1702029171_tasks.sql:2
CREATE TABLE IF NOT EXISTS _tasks
(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    header character varying(255) NOT NULL,
    description character varying(255) NOT NULL,
    priority character varying(255) NOT NULL,
    status character varying(255) NOT NULL,
    create_date timestamp without time zone,
    update_date timestamp without time zone,
    user_author_fk BIGINT NOT NULL,
    user_performer_fk BIGINT NOT NULL,
    -- CONSTRAINT _tasks_pkey PRIMARY KEY (id),
    CONSTRAINT _user_author_fk_1 FOREIGN KEY (user_author_fk)
            REFERENCES public._users (id) MATCH SIMPLE
            ON UPDATE NO ACTION
            ON DELETE NO ACTION,
        CONSTRAINT _user_performer_fk_1 FOREIGN KEY (user_performer_fk)
            REFERENCES public._users (id) MATCH SIMPLE
            ON UPDATE NO ACTION
            ON DELETE NO ACTION
);
