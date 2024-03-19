package org.egualpam.services.hotelmanagement.domain.shared;

import java.time.Instant;
import java.util.UUID;

public interface DomainEvent {
    UUID getId();

    AggregateId getAggregateId();

    Instant getOccurredOn();

    String getType();
}
