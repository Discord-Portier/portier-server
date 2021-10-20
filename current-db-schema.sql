-- This is supposed to represent the current SQL schema.
-- Usage of this file is required as it best maps the Kotlin entity classes.

CREATE SCHEMA portier;

CREATE TABLE portier.users
(
    id         uuid PRIMARY KEY,
    password   bytea       NOT NULL,
    salt       bytea       NOT NULL,
    creator_id uuid,
    modified   timestamptz NOT NULL DEFAULT NOW(),
    created    timestamptz NOT NULL DEFAULT NOW(),

    CONSTRAINT creator_id_fk
        FOREIGN KEY (creator_id)
            REFERENCES portier.users (id)
);

CREATE TYPE portier.user_permission
AS ENUM (
    'READ_PUNISHMENTS',
    'WRITE_PUNISHMENTS',
    'LIFT_PUNISHMENTS',
    'DELETE_PUNISHMENTS',
    'LIFT_OTHERS_PUNISHMENTS',
    'DELETE_OTHERS_PUNISHMENTS',

    'READ_SERVERS',
    'WRITE_SERVERS',

    'READ_USER_LIST',
    'CREATE_USER',
    'DELETE_USER',
    'MODIFY_USER',

    'READ_ACTORS',
    'CREATE_ACTORS'
    );
CREATE CAST (VARCHAR AS portier.user_permission) WITH INOUT AS IMPLICIT;
CREATE TABLE portier.user_permissions
(
    id         uuid PRIMARY KEY,
    permission portier.user_permission NOT NULL,
    user_id    uuid                    NOT NULL,
    creator_id uuid                    NOT NULL,
    modified   timestamptz             NOT NULL DEFAULT NOW(),
    created    timestamptz             NOT NULL DEFAULT NOW(),

    CONSTRAINT user_id_fk
        FOREIGN KEY (user_id)
            REFERENCES portier.users (id)
            ON DELETE CASCADE,
    CONSTRAINT creator_id_fk
        FOREIGN KEY (creator_id)
            REFERENCES portier.users (id)
);

CREATE TABLE portier.servers
(
    id         BIGINT PRIMARY KEY,
    name       VARCHAR     NOT NULL,
    trusted    BOOLEAN     NOT NULL DEFAULT FALSE,
    creator_id uuid        NOT NULL,
    modified   timestamptz NOT NULL DEFAULT NOW(),
    created    timestamptz NOT NULL DEFAULT NOW(),

    CONSTRAINT creator_id_fk
        FOREIGN KEY (creator_id)
            REFERENCES portier.users (id)
);

CREATE TABLE portier.actors
(
    id            BIGINT PRIMARY KEY,
    username      TEXT        NOT NULL,
    discriminator INT         NOT NULL,
    creator_id    uuid        NOT NULL,
    modified      timestamptz NOT NULL DEFAULT NOW(),
    created       timestamptz NOT NULL DEFAULT NOW(),

    CONSTRAINT creator_id_fk
        FOREIGN KEY (creator_id)
            REFERENCES portier.users (id)
);

CREATE TYPE portier.infraction_category
AS ENUM (
    'INHUMANE_BEHAVIOUR',
    'RACISM',
    'DEATH_THREATS',
    'DOXXING',
    'OTHER'
    );
CREATE CAST (VARCHAR AS portier.infraction_category) WITH INOUT AS IMPLICIT;
CREATE TABLE portier.infractions
(
    id          uuid PRIMARY KEY,
    target_id   BIGINT                      NOT NULL,
    punisher_id BIGINT                      NOT NULL,
    server_id   BIGINT                      NOT NULL,
    creator_id  uuid                        NOT NULL,
    reason      TEXT                        NOT NULL,
    category    portier.infraction_category NOT NULL,
    lifted      BOOLEAN                     NOT NULL DEFAULT FALSE,
    hidden      BOOLEAN                     NOT NULL DEFAULT FALSE,
    modified    timestamptz                 NOT NULL DEFAULT NOW(),
    created     timestamptz                 NOT NULL DEFAULT NOW(),

    CONSTRAINT server_id_fk
        FOREIGN KEY (server_id)
            REFERENCES portier.servers (id),
    CONSTRAINT target_id_fk
        FOREIGN KEY (target_id)
            REFERENCES portier.actors (id),
    CONSTRAINT punisher_id_fk
        FOREIGN KEY (punisher_id)
            REFERENCES portier.actors (id),
    CONSTRAINT creator_id_fk
        FOREIGN KEY (creator_id)
            REFERENCES portier.users (id)
);
CREATE INDEX infractions_target_id_idx
    ON portier.infractions (target_id);

CREATE TABLE portier.infraction_evidence
(
    id            uuid PRIMARY KEY,
    infraction_id uuid        NOT NULL,
    creator_id    uuid        NOT NULL,
    value         TEXT        NOT NULL,
    created       timestamptz NOT NULL DEFAULT NOW(),
    modified      timestamptz NOT NULL DEFAULT NOW(),

    CONSTRAINT infraction_id_fk
        FOREIGN KEY (infraction_id)
            REFERENCES portier.infractions (id),
    CONSTRAINT creator_id_fk
        FOREIGN KEY (creator_id)
            REFERENCES portier.users (id)
);
