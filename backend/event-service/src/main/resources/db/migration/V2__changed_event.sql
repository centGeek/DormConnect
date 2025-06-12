ALTER TABLE event_organizer
DROP
CONSTRAINT fk_event_organizer_on_event_entity;

ALTER TABLE event
    ADD organizer_id BIGINT;

DROP TABLE event_organizer CASCADE;
