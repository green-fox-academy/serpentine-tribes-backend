/*
 * PostgreSQL initialization script with Flyway
 */

/*
 * Sequences
 */
CREATE SEQUENCE public.hibernate_sequence
    INCREMENT 100
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

CREATE SEQUENCE public.user_sequence
    INCREMENT 50
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

CREATE SEQUENCE public.kingdom_sequence
    INCREMENT 50
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

CREATE SEQUENCE public.building_sequence
    INCREMENT 50
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

CREATE SEQUENCE public.resource_sequence
    INCREMENT 50
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

CREATE SEQUENCE public.troop_sequence
    INCREMENT 50
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

CREATE SEQUENCE public.location_sequence
    INCREMENT 50
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;


/*
 * Table: user
 */
CREATE TABLE public."user"
(
    id bigint NOT NULL,
    username character varying(255) COLLATE pg_catalog."default",
    password character varying(255) COLLATE pg_catalog."default",
    avatar character varying(255) COLLATE pg_catalog."default",
    points bigint NOT NULL,
    CONSTRAINT user_pkey PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;


/*
 * Table: kingdom
 */
CREATE TABLE public.kingdom
(
    id bigint NOT NULL,
    user_id bigint,
    name character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT kingdom_pkey PRIMARY KEY (id),
    CONSTRAINT "FKj8kngpiduk6760ra9gemii7g2" FOREIGN KEY (user_id)
        REFERENCES public."user" (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;


/*
 * Table: building
 */
CREATE TABLE public.building
(
    id bigint NOT NULL,
    kingdom_id bigint,
    type character varying(255) COLLATE pg_catalog."default",
    level integer NOT NULL,
    hp integer NOT NULL,
    CONSTRAINT building_pkey PRIMARY KEY (id),
    CONSTRAINT "FKka1dx67cbggx8un4j11b0oofr" FOREIGN KEY (kingdom_id)
        REFERENCES public.kingdom (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;


/*
 * Table: resource
 */
CREATE TABLE public.resource
(
    id bigint NOT NULL,
    kingdom_id bigint,
    type character varying(255) COLLATE pg_catalog."default",
    amount integer NOT NULL,
    generation integer NOT NULL,
    CONSTRAINT resource_pkey PRIMARY KEY (id),
    CONSTRAINT "FK1va5vmk8bnu15bar6scxpa0j" FOREIGN KEY (kingdom_id)
        REFERENCES public.kingdom (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;


/*
 * Table: troop
 */
CREATE TABLE public.troop
(
    id bigint NOT NULL,
    kingdom_id bigint,
    level integer NOT NULL,
    hp integer NOT NULL,
    attack integer NOT NULL,
    defence integer NOT NULL,
    CONSTRAINT troop_pkey PRIMARY KEY (id),
    CONSTRAINT "FKr4lttcwkqkv5mfnv2iaqgj4v7" FOREIGN KEY (kingdom_id)
        REFERENCES public.kingdom (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;


/*
 * Table: location
 */
CREATE TABLE public.location
(
    id bigint NOT NULL,
    kingdom_id bigint,
    x integer,
    y integer,
    CONSTRAINT location_pkey PRIMARY KEY (id),
    CONSTRAINT "FKiwnjdeojrc06rxuh99xoefdvv" FOREIGN KEY (kingdom_id)
        REFERENCES public.kingdom (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;
