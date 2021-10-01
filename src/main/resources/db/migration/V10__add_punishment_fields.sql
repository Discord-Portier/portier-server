ALTER TABLE portier.punishments
    ADD COLUMN reason TEXT NOT NULL;

CREATE TYPE portier.punishment_category
AS ENUM (
    'INHUMANE_BEHAVIOUR',
    'RACISM',
    'DEATH_THREATS',
    'DOXXING',
    'OTHER'
    );
CREATE CAST (VARCHAR AS portier.punishment_category) WITH INOUT AS IMPLICIT;
ALTER TABLE portier.punishments
    ADD COLUMN category portier.punishment_category NOT NULL;

CREATE TABLE portier.punishment_evidence
(
    id            uuid PRIMARY KEY,
    punishment_id uuid        NOT NULL,
    creator_id    uuid        NOT NULL,
    value         TEXT        NOT NULL,
    created       timestamptz NOT NULL DEFAULT NOW(),
    modified      timestamptz NOT NULL DEFAULT NOW(),

    CONSTRAINT punishment_id_fk
        FOREIGN KEY (punishment_id)
            REFERENCES portier.punishments (id),
    CONSTRAINT creator_id_fk
        FOREIGN KEY (creator_id)
            REFERENCES portier.users (id)
);
