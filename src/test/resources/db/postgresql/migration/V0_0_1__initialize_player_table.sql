/*
 * PostgreSQL initialization script with Flyway
 */

/*
 * Structure
 */

CREATE SEQUENCE public.hibernate_sequence
INCREMENT 1
START 1
MINVALUE 1
MAXVALUE 9223372036854775807
CACHE 1;


CREATE TABLE public.player
(
    id bigint NOT NULL,
    kingdom_id bigint NOT NULL,
    username character varying(255) COLLATE pg_catalog."default",
    password character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT player_pkey PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

/*
 * Data
 */

INSERT INTO public.player(
	id, kingdom_id, username, password)
VALUES ('1','1','Bond','password123');