package org.egualpam.services.hotel.rating.domain.shared;

import java.time.Instant;
import java.util.UUID;

public interface DomainEvent {
    UUID getId();

    AggregateId getAggregateId();

    Instant getOccurredOn();

    String getType();
}
