package org.egualpam.services.hotelmanagement.reviews.domain;

import org.egualpam.services.hotelmanagement.shared.domain.AggregateId;
import org.egualpam.services.hotelmanagement.shared.domain.DomainEvent;

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
}
