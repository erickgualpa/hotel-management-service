CREATE TABLE hotel_average_rating
(
    hotel_id        UUID NOT NULL,
    rating_sum      INT NOT NULL DEFAULT 0,
    review_count    INT NOT NULL DEFAULT 0,
    avg_value       NUMERIC NOT NULL DEFAULT 0.0,
    PRIMARY KEY (hotel_id)
);