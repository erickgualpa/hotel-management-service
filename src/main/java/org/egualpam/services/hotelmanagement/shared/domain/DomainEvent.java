package org.egualpam.services.hotelmanagement.shared.domain;

import java.time.Instant;
import java.util.UUID;

public interface DomainEvent {
    UUID getId();

    AggregateId getAggregateId();

    Instant getOccurredOn();

    String getType();
}
