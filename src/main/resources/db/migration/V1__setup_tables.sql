CREATE SCHEMA IF NOT EXISTS portier;

CREATE TABLE portier.servers
(
    id      bigint primary key,
    name    varchar not null,
    trusted boolean not null default false
);

CREATE TABLE portier.punishments
(
    id          uuid primary key,
    target_id   bigint not null,
    punisher_id bigint not null,
    server_id   bigint not null
);
CREATE INDEX punishments_target_id_idx
    ON portier.punishments (target_id);
