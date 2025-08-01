CREATE TABLE reservation
(
    id              UUID NOT NULL,
    room_id         UUID NOT NULL REFERENCES room(id),
    reserved_from   DATE NOT NULL,
    reserved_to     DATE NOT NULL,
    PRIMARY KEY (id)
);