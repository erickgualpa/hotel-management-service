package org.egualpam.services.hotel.rating.domain.reviews;

import org.egualpam.services.hotel.rating.domain.shared.AggregateId;
import org.egualpam.services.hotel.rating.domain.shared.DomainEvent;

import java.time.Instant;

public class ReviewUpdated implements DomainEvent {

    private final AggregateId aggregateId;
    private final Instant occurredOn;

    public ReviewUpdated(Review review) {
        this.aggregateId = review.getId();
        this.occurredOn = Instant.now();
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
