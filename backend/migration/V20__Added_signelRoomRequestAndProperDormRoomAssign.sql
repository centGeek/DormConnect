CREATE SEQUENCE IF NOT EXISTS single_room_requests_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE dorm_rooms_room_assigns
(
    room_entity_id  BIGINT NOT NULL,
    room_assigns_id BIGINT NOT NULL
);

CREATE TABLE single_room_requests
(
    id               BIGINT                      NOT NULL,
    student_id       BIGINT                      NOT NULL,
    current_room_id  BIGINT                      NOT NULL,
    only_single_room BOOLEAN                     NOT NULL,
    status           VARCHAR(255)                NOT NULL,
    created_at       TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    responded_at     TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_single_room_requests PRIMARY KEY (id)
);

ALTER TABLE room_replacement_forms
    ADD requester_room_id BIGINT;

ALTER TABLE room_replacement_forms
    ADD target_room_id BIGINT;

ALTER TABLE dorm_rooms_room_assigns
    ADD CONSTRAINT uc_dorm_rooms_room_assigns_roomassigns UNIQUE (room_assigns_id);

ALTER TABLE grouped_rooms_entity_rooms
    ADD CONSTRAINT uc_grouped_rooms_entity_rooms_rooms UNIQUE (rooms_id);

ALTER TABLE dorm_rooms_room_assigns
    ADD CONSTRAINT fk_dorroorooass_on_room_assign_entity FOREIGN KEY (room_assigns_id) REFERENCES room_assign_entity (id);

ALTER TABLE dorm_rooms_room_assigns
    ADD CONSTRAINT fk_dorroorooass_on_room_entity FOREIGN KEY (room_entity_id) REFERENCES dorm_rooms (id);

ALTER TABLE grouped_rooms_entity_rooms
    ADD CONSTRAINT fk_grorooentroo_on_grouped_rooms_entity FOREIGN KEY (grouped_rooms_entity_id) REFERENCES grouped_rooms_entity (id);

ALTER TABLE grouped_rooms_entity_rooms
    ADD CONSTRAINT fk_grorooentroo_on_room_entity FOREIGN KEY (rooms_id) REFERENCES dorm_rooms (id);

ALTER TABLE room_replacement_forms
    DROP COLUMN requester_room;

ALTER TABLE room_replacement_forms
    DROP COLUMN target_room;

ALTER TABLE room_replacement_forms
    ALTER COLUMN requester_id SET NOT NULL;