package org.egualpam.services.hotelmanagement.review.domain;

import org.egualpam.services.hotelmanagement.shared.domain.AggregateId;
import org.egualpam.services.hotelmanagement.shared.domain.DomainEvent;
import org.egualpam.services.hotelmanagement.shared.domain.UniqueId;

import java.time.Instant;

public final class ReviewUpdated implements DomainEvent {

    private final UniqueId id;
    private final AggregateId aggregateId;
    private final Instant occurredOn;

    public ReviewUpdated(AggregateId aggregateId, Instant occurredOn) {
        this.id = UniqueId.get();
        this.aggregateId = aggregateId;
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
