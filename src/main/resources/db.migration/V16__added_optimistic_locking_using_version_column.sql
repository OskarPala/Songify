ALTER TABLE album
    ADD version BIGINT DEFAULT 0;

ALTER TABLE album
    ALTER COLUMN version SET NOT NULL;

ALTER TABLE artist
    ADD version BIGINT DEFAULT 0;

ALTER TABLE artist
    ALTER COLUMN version SET NOT NULL;

ALTER TABLE genre
    ADD version BIGINT DEFAULT 0;

ALTER TABLE genre
    ALTER COLUMN version SET NOT NULL;

ALTER TABLE song
    ADD version BIGINT DEFAULT 0;

ALTER TABLE song
    ALTER COLUMN version SET NOT NULL;