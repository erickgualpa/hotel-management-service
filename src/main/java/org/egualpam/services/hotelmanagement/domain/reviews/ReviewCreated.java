package org.egualpam.services.hotelmanagement.domain.reviews;

import org.egualpam.services.hotelmanagement.shared.domain.AggregateId;
import org.egualpam.services.hotelmanagement.shared.domain.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public final class ReviewCreated implements DomainEvent {

    private final UUID id;
    private final AggregateId aggregateId;
    private final Instant occurredOn;

    public ReviewCreated(AggregateId id, Instant occurredOn) {
        this.id = UUID.randomUUID();
        this.aggregateId = id;
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
        return "domain.review.created.v1.0";
    }
}
