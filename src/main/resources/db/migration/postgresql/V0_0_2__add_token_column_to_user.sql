/*
Token column to User table
 */
ALTER TABLE public."user"
    ADD COLUMN token character varying(255) COLLATE pg_catalog."default";