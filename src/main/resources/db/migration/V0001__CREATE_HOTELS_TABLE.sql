CREATE TABLE hotels
(
    -- TODO: Review function used to generate uuid identifier
    id          uuid DEFAULT gen_random_uuid(),
    name        VARCHAR(255) NOT NULL,
    description TEXT,
    location    VARCHAR(255) NOT NULL,
    total_price BIGINT       NOT NULL,
    image_url   VARCHAR(255),
    PRIMARY KEY (id)
);