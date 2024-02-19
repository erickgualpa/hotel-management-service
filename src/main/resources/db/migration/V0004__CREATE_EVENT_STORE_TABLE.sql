CREATE TABLE event_store
(
    id           uuid DEFAULT gen_random_uuid(),
    aggregate_id uuid NOT NULL,
    event_type   TEXT,
    PRIMARY KEY (id)
);