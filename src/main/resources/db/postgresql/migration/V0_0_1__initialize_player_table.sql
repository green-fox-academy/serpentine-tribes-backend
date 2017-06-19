/*
 * PostgreSQL initialization script with Flyway
 */

/*
 * Structure
 */

CREATE SEQUENCE hibernate_sequence
INCREMENT 1
START 1
MINVALUE 1
MAXVALUE 9223372036854775807
CACHE 1;


CREATE TABLE player
(
    id bigint NOT NULL,
    kingdom_id bigint NOT NULL,
    username character varying(255),
    password character varying(255),
    CONSTRAINT player_pkey PRIMARY KEY (id)
);

/*
 * Data
 */

INSERT INTO player(
	id, kingdom_id, username, password)
VALUES ('1','1','Bond','password123');