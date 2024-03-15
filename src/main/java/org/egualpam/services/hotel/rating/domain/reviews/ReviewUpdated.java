package org.egualpam.services.hotel.rating.domain.reviews;

import org.egualpam.services.hotel.rating.domain.shared.AggregateId;
import org.egualpam.services.hotel.rating.domain.shared.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public final class ReviewUpdated implements DomainEvent {

    private final UUID id;
    private final AggregateId aggregateId;
    private final Instant occurredOn;

    public ReviewUpdated(AggregateId aggregateId, Instant occurredOn) {
        this.id = UUID.randomUUID();
        this.aggregateId = aggregateId;
        this.occurredOn = occurredOn;
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public AggregateId getAggregateId() {
        return this.aggregateId;
    }

    @Override
    public Instant getOccurredOn() {
        return this.occurredOn;
    }

    @Override
    public String getType() {
        return "domain.review.updated.v1.0";
    }
}
