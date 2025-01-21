CREATE TABLE event_store
(
    id            uuid                     DEFAULT gen_random_uuid(),
    aggregate_id  uuid                     NOT NULL,
    occurred_on   timestamp with time zone NOT NULL,
    event_type    TEXT                     NOT NULL,
    event_version varchar(5)               NOT NULL,
    event_payload jsonb                    NOT NULL,
    PRIMARY KEY (id)
);