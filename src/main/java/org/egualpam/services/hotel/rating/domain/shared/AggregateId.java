package org.egualpam.services.hotel.rating.domain.shared;

import java.util.UUID;

public record AggregateId(UUID value) {
    public AggregateId(String value) {
        this(UUID.fromString(value));
    }
}
