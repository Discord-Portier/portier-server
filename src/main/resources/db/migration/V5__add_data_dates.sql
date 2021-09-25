ALTER TABLE portier.users
    ADD COLUMN modified timestamptz not null default now();
ALTER TABLE portier.users
    ADD COLUMN created timestamptz not null default now();
ALTER TABLE portier.user_permissions
    ADD COLUMN modified timestamptz not null default now();
ALTER TABLE portier.user_permissions
    ADD COLUMN created timestamptz not null default now();
ALTER TABLE portier.servers
    ADD COLUMN modified timestamptz not null default now();
ALTER TABLE portier.servers
    ADD COLUMN created timestamptz not null default now();
ALTER TABLE portier.punishments
    ADD COLUMN modified timestamptz not null default now();
ALTER TABLE portier.punishments
    ADD COLUMN created timestamptz not null default now();
