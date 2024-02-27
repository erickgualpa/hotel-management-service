package org.egualpam.services.hotel.rating.domain.reviews;

import org.egualpam.services.hotel.rating.domain.shared.AggregateId;
import org.egualpam.services.hotel.rating.domain.shared.DomainEvent;

import java.time.Instant;

public final class ReviewCreated implements DomainEvent {

    private final AggregateId id;
    private final Instant occurredOn;

    public ReviewCreated(Review review) {
        this.id = review.getId();
        this.occurredOn = Instant.now();
    }

    @Override
    public AggregateId getAggregateId() {
        return this.id;
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
