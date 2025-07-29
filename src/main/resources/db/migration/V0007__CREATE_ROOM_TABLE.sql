CREATE TABLE room
(
    id              UUID NOT NULL,
    hotel_id        UUID NOT NULL REFERENCES hotels(id),
    room_type       VARCHAR(5) NOT NULL,
    PRIMARY KEY (id)
);