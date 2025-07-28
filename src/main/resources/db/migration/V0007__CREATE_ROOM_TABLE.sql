CREATE TABLE room
(
    id              UUID NOT NULL,
    hotel_id        UUID NOT NULL,
    room_type       VARCHAR(5) NOT NULL,
    PRIMARY KEY (id)
);