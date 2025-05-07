ALTER TABLE common_room_assigment
DROP
CONSTRAINT fk_commonroomassigment_on_room;

ALTER TABLE dorm_rooms_room_assigns
DROP
CONSTRAINT fk_dorroorooass_on_room_assign_entity;

ALTER TABLE dorm_rooms_room_assigns
DROP
CONSTRAINT fk_dorroorooass_on_room_entity;

ALTER TABLE grouped_rooms_entity_rooms
DROP
CONSTRAINT fk_grorooentroo_on_grouped_rooms_entity;

ALTER TABLE grouped_rooms_entity_rooms
DROP
CONSTRAINT fk_grorooentroo_on_room_entity;

ALTER TABLE common_room_assigment
    ADD common_room_id BIGINT;

ALTER TABLE common_room_assigment
    ADD CONSTRAINT FK_COMMONROOMASSIGMENT_ON_COMMON_ROOM FOREIGN KEY (common_room_id) REFERENCES common_room (common_room_id);

DROP TABLE dorm_rooms_room_assigns CASCADE;

DROP TABLE grouped_rooms_entity_rooms CASCADE;

ALTER TABLE common_room_assigment
DROP
COLUMN room_id;