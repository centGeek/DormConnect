ALTER TABLE event
    ADD approval_status VARCHAR(100) DEFAULT 'WAITING';

ALTER TABLE event
    ALTER COLUMN approval_status SET NOT NULL;

ALTER TABLE event
    DROP COLUMN is_approved;