package org.egualpam.services.hotelmanagement.shared.domain;

import java.util.UUID;

// TODO: Avoid having UUID at this level
public record AggregateId(UUID value) {
    public AggregateId(String value) {
        this(new UniqueId(value).value());
    }
}
