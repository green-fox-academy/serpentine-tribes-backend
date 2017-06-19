/*
 * PostgreSQL addition script with Flyway
 */

/*
 * Structure - Building model
 */
CREATE TABLE building
(
    id bigint NOT NULL,
    building_type character varying(255),
    level integer NOT NULL,
    hp integer NOT NULL,
    CONSTRAINT building_pkey PRIMARY KEY (id)
);

/*
 * Structure - Troop model
 */
CREATE TABLE troop
(
    id bigint NOT NULL,
    level integer NOT NULL,
    hp integer NOT NULL,
    attack integer NOT NULL,
    defence integer NOT NULL,
    CONSTRAINT troop_pkey PRIMARY KEY (id)
);

/*
 * Structure - Resource model
 */
CREATE TABLE resource
(
    id bigint NOT NULL,
    type character varying(255),
    amount integer NOT NULL,
    generation integer NOT NULL,
    CONSTRAINT resource_pkey PRIMARY KEY (id)
);

/*
 * Structure - Location model
 */
CREATE TABLE location
(
    id bigint NOT NULL,
    x integer NOT NULL,
    y integer NOT NULL,
    CONSTRAINT location_pkey PRIMARY KEY (id)
);
