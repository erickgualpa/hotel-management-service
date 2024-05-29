package org.egualpam.services.hotelmanagement.shared.infrastructure.eventbus.events;

import java.time.Instant;

public interface PublicEvent {
    String getId();

    String getType();

    String getAggregateId();

    Instant getOccurredOn();
}
