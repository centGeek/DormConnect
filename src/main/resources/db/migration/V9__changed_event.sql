ALTER TABLE dorm_rooms_room_assigns
DROP
CONSTRAINT fk_dorroorooass_on_room_assign_entity;

ALTER TABLE dorm_rooms_room_assigns
DROP
CONSTRAINT fk_dorroorooass_on_room_entity;

ALTER TABLE event_organizer
DROP
CONSTRAINT fk_event_organizer_on_event_entity;

ALTER TABLE grouped_rooms_entity_rooms
DROP
CONSTRAINT fk_grorooentroo_on_grouped_rooms_entity;

ALTER TABLE grouped_rooms_entity_rooms
DROP
CONSTRAINT fk_grorooentroo_on_room_entity;

ALTER TABLE event
    ADD organizer_id BIGINT;

DROP TABLE dorm_rooms_room_assigns CASCADE;

DROP TABLE event_organizer CASCADE;

DROP TABLE grouped_rooms_entity_rooms CASCADE;