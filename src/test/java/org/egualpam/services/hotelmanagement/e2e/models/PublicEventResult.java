package org.egualpam.services.hotelmanagement.e2e.models;

import java.time.Instant;
import java.util.UUID;

public record PublicEventResult(
        UUID id,
        UUID aggregateId,
        Instant occurredOn,
        String type
) {
}
