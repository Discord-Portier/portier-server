ALTER TABLE portier.punishments
    ADD CONSTRAINT server_id_fk
        FOREIGN KEY (server_id)
            REFERENCES portier.servers (id);

CREATE TABLE portier.actors
(
    id            bigint primary key,
    username      text not null,
    discriminator int  not null,
    creator_id    uuid not null,

    CONSTRAINT creator_id_fk
        foreign key (creator_id)
            references portier.users (id)
);

ALTER TABLE portier.punishments
    ADD CONSTRAINT target_id_fk
        FOREIGN KEY (target_id)
            REFERENCES portier.actors (id);
ALTER TABLE portier.punishments
    ADD CONSTRAINT punisher_id_fk
        FOREIGN KEY (punisher_id)
            REFERENCES portier.actors (id);
