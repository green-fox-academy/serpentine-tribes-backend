/*
Token column to Troop table
 */
ALTER TABLE public."troop"
    ADD COLUMN timestamp character varying(255) COLLATE pg_catalog."default";