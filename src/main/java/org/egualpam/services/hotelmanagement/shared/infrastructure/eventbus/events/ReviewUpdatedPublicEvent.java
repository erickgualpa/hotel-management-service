package org.egualpam.services.hotelmanagement.shared.infrastructure.eventbus.events;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.Instant;

@JsonSerialize
public final class ReviewUpdatedPublicEvent implements PublicEvent {

    private final String id;
    private final String type;
    private final String aggregateId;
    private final Instant occurredOn;

    public ReviewUpdatedPublicEvent(String id, String aggregateId, Instant occurredOn) {
        this.id = id;
        this.type = "hotelmanagement.reviews.updated.v1.0";
        this.aggregateId = aggregateId;
        this.occurredOn = occurredOn;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getAggregateId() {
        return aggregateId;
    }

    @Override
    public Instant getOccurredOn() {
        return occurredOn;
    }
}
