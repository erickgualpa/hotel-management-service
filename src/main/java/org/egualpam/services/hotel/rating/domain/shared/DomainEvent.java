package org.egualpam.services.hotel.rating.domain.shared;

import java.time.Instant;

public interface DomainEvent {
    AggregateId getAggregateId();

    Instant getOccurredOn();

    String getType();
}
