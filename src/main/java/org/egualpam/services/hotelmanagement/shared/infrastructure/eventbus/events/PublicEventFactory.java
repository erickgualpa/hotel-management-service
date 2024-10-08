package org.egualpam.services.hotelmanagement.shared.infrastructure.eventbus.events;

import org.egualpam.services.hotelmanagement.review.domain.ReviewCreated;
import org.egualpam.services.hotelmanagement.review.domain.ReviewUpdated;
import org.egualpam.services.hotelmanagement.shared.domain.DomainEvent;

public final class PublicEventFactory {

    private PublicEventFactory() {
    }

    public static PublicEvent from(DomainEvent domainEvent) {
        if (domainEvent instanceof ReviewCreated) {
            return new ReviewCreatedPublicEvent(
                    domainEvent.getId().value(),
                    domainEvent.getAggregateId().value(),
                    domainEvent.getOccurredOn()
            );
        } else if (domainEvent instanceof ReviewUpdated) {
            return new ReviewUpdatedPublicEvent(
                    domainEvent.getId().toString(),
                    domainEvent.getAggregateId().value(),
                    domainEvent.getOccurredOn()
            );
        } else {
            throw new UnsupportedDomainEvent(domainEvent);
        }
    }
}
