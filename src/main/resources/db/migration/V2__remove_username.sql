-- We will now just identify using user IDs.
ALTER TABLE portier.users
    DROP CONSTRAINT username_uidx;
ALTER TABLE portier.users
    DROP COLUMN username;
