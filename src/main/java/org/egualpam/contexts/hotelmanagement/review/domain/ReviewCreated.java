package org.egualpam.contexts.hotelmanagement.review.domain;

import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateId;
import org.egualpam.contexts.hotelmanagement.shared.domain.DomainEvent;
import org.egualpam.contexts.hotelmanagement.shared.domain.UniqueId;

import java.time.Instant;

public final class ReviewCreated implements DomainEvent {

    private final UniqueId id;
    private final AggregateId aggregateId;
    private final Instant occurredOn;

    public ReviewCreated(AggregateId id, Instant occurredOn) {
        this.id = UniqueId.get();
        this.aggregateId = id;
        this.occurredOn = occurredOn;
    }

    @Override
    public UniqueId getId() {
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
