ALTER TABLE portier.user_permissions
    ADD COLUMN creator_id uuid NOT NULL,
    ADD CONSTRAINT creator_id_fk
        FOREIGN KEY (creator_id)
            REFERENCES portier.users (id);
