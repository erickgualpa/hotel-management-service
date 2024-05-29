package org.egualpam.services.hotelmanagement.shared.domain;

import java.time.Instant;
import java.util.UUID;

public interface DomainEvent {
    // TODO: Use 'UniqueId' instead of 'UUID'
    UUID getId();

    AggregateId getAggregateId();

    Instant getOccurredOn();
}
