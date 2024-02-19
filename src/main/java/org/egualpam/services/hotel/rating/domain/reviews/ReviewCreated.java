package org.egualpam.services.hotel.rating.domain.reviews;

import org.egualpam.services.hotel.rating.domain.shared.DomainEvent;
import org.egualpam.services.hotel.rating.domain.shared.Identifier;

public class ReviewCreated implements DomainEvent {

    private final Identifier aggregateId;

    public ReviewCreated(Identifier aggregateId) {
        this.aggregateId = aggregateId;
    }

    @Override
    public Identifier getAggregateId() {
        return this.aggregateId;
    }

    @Override
    public String getType() {
        return "domain.review.created.v1.0";
    }
}
