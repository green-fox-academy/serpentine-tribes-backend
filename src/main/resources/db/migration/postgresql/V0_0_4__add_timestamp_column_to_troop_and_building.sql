/*
Timestamp column to Troop table
 */
ALTER TABLE public."troop"
    ADD COLUMN "started_at" timestamp with time zone;

/*
Timestamp column to Building table
 */
ALTER TABLE public."building"
    ADD COLUMN "started_at" timestamp with time zone;