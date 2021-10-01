-- This is technically not legal, but no data should exist by now as this is still in development.
-- Test DBs will simply have to be dropped or otherwise fixed.
ALTER TABLE portier.servers
    ADD COLUMN creator_id uuid NOT NULL;
ALTER TABLE portier.servers
    ADD CONSTRAINT creator_id_fk
        FOREIGN KEY (creator_id)
            REFERENCES portier.users (id);
ALTER TABLE portier.users
    ADD COLUMN creator_id uuid;
ALTER TABLE portier.users
    ADD CONSTRAINT creator_id_fk
        FOREIGN KEY (creator_id)
            REFERENCES portier.users (id);
