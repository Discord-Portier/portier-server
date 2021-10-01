ALTER TABLE portier.actors
    ADD COLUMN modified timestamptz not null default now();
ALTER TABLE portier.actors
    ADD COLUMN created timestamptz not null default now();
