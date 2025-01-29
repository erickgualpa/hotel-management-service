CREATE TABLE hotel_rating
(
    id              UUID NOT NULL,
    hotel_id        UUID NOT NULL,
    rating_sum      INT NOT NULL DEFAULT 0,
    review_count    INT NOT NULL DEFAULT 0,
    avg_value       NUMERIC NOT NULL DEFAULT 0.0,
    PRIMARY KEY (id)
);