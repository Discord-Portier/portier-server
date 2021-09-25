-- This is technically not legal, but no data should exist by now as this is still in development.
-- Test DBs will simply have to be dropped or otherwise fixed.
ALTER TABLE portier.punishments
    ADD COLUMN creator_id uuid not null;
ALTER TABLE portier.punishments
    ADD CONSTRAINT creator_id_fk
        foreign key (creator_id)
            references portier.users (id);
