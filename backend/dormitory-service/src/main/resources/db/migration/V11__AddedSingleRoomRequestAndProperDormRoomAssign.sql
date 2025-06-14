CREATE SEQUENCE IF NOT EXISTS single_room_requests_seq START WITH 1 INCREMENT BY 50;

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

ALTER TABLE room_replacement_forms
    DROP COLUMN requester_room;

ALTER TABLE room_replacement_forms
    DROP COLUMN target_room;

ALTER TABLE room_replacement_forms
    ALTER COLUMN requester_id SET NOT NULL;