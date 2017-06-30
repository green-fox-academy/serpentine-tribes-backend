/*
Change tables: set NOT NULL on foreign key columns
 */
ALTER TABLE public.kingdom
    ALTER COLUMN user_id SET NOT NULL;

ALTER TABLE public.building
    ALTER COLUMN kingdom_id SET NOT NULL;

ALTER TABLE public.resource
    ALTER COLUMN kingdom_id SET NOT NULL;

ALTER TABLE public.troop
    ALTER COLUMN kingdom_id SET NOT NULL;

ALTER TABLE public.location
    ALTER COLUMN kingdom_id SET NOT NULL;
