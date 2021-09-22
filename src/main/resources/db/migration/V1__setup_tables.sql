CREATE SCHEMA IF NOT EXISTS portier;

CREATE TABLE portier.users
(
    id       uuid primary key,
    username text  not null,
    password bytea not null,
    salt     bytea not null,

    CONSTRAINT username_uidx
        UNIQUE (username)
);

CREATE TABLE portier.user_permissions
(
    id         uuid primary key,
    permission text not null,
    user_id    uuid not null,

    CONSTRAINT user_id_fk
        FOREIGN KEY (user_id)
            REFERENCES portier.users (id)
            ON DELETE CASCADE
);

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
