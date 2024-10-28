CREATE TABLE hotels
(
    id              uuid NOT NULL,
    name            VARCHAR(255) NOT NULL,
    description     TEXT,
    location        VARCHAR(255) NOT NULL,
    price           BIGINT NOT NULL,
    image_url       VARCHAR(255),
    PRIMARY KEY (id)
);