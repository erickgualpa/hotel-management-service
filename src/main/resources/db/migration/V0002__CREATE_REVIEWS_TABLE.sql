CREATE TABLE reviews
(
    id       uuid DEFAULT gen_random_uuid(),
    rating   BIGINT,
    comment  TEXT,
    hotel_id uuid NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (hotel_id) REFERENCES hotels (id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);