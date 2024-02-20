package org.egualpam.services.hotel.rating.domain.reviews;

import org.egualpam.services.hotel.rating.domain.shared.AggregateId;
import org.egualpam.services.hotel.rating.domain.shared.DomainEvent;

public class ReviewCreated implements DomainEvent {

    private final AggregateId id;

    public ReviewCreated(AggregateId id) {
        this.id = id;
    }

    @Override
    public AggregateId getAggregateId() {
        return this.id;
    }

    @Override
    public String getType() {
        return "domain.review.created.v1.0";
    }
}
