package org.egualpam.services.hotelmanagement.shared.domain;

import java.time.Instant;

public interface DomainEvent {
    UniqueId getId();

    AggregateId getAggregateId();

    Instant getOccurredOn();
}
