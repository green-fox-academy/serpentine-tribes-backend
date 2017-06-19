/*
 * PostgreSQL addition script with Flyway
 */

/*
 * Structure - Kingdom model
 */
CREATE TABLE kingdom
(
    id bigint NOT NULL,
    name character varying(255),
    user_id bigint,
    CONSTRAINT kingdom_pkey PRIMARY KEY (id)
);
