package org.egualpam.services.hotelmanagement.domain.shared;

import org.egualpam.services.hotelmanagement.domain.shared.exception.InvalidIdentifier;

import java.util.UUID;

public record AggregateId(UUID value) {

    public AggregateId(String value) {
        this(valid(value));
    }

    private static UUID valid(String value) {
        try {
            return UUID.fromString(value);
        } catch (Exception e) {
            throw new InvalidIdentifier(e);
        }
    }
}
