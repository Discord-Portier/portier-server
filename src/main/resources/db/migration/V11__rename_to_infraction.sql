ALTER TABLE portier.punishments
    RENAME TO infractions;
ALTER INDEX portier.punishments_target_id_idx
    RENAME TO infractions_target_id_idx;
ALTER TABLE portier.punishment_evidence
    RENAME TO infraction_evidence;
ALTER TYPE portier.punishment_category
    RENAME TO infraction_category;
ALTER TABLE portier.infraction_evidence
    RENAME COLUMN punishment_id TO infraction_id;
ALTER TABLE portier.infraction_evidence
    RENAME CONSTRAINT punishment_id_fk TO infraction_id_fk;
ALTER INDEX portier.punishment_evidence_pkey
    RENAME TO infraction_evidence_pkey;
