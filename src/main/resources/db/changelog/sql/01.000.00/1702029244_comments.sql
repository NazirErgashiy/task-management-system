--liquibase formatted sql

--changeset 1702029244_comments.sql:3
CREATE TABLE IF NOT EXISTS _comments
(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    description character varying(255) NOT NULL,
    create_date timestamp without time zone,
    update_date timestamp without time zone,
    author_fk bigint NOT NULL,
    task_fk bigint NOT NULL,
    -- CONSTRAINT _comments_pkey PRIMARY KEY (id),
    CONSTRAINT _task_fk_1 FOREIGN KEY (task_fk)
        REFERENCES public._tasks (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT _comment_author_fk_1 FOREIGN KEY (author_fk)
        REFERENCES public._users (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);