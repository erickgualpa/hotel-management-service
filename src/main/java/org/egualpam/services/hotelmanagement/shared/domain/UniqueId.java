package org.egualpam.services.hotelmanagement.shared.domain;

import org.egualpam.services.hotelmanagement.shared.domain.exceptions.InvalidUniqueId;

import java.util.UUID;

public record UniqueId(UUID value) {

    public UniqueId(String value) {
        this(valid(value));
    }

    private static UUID valid(String value) {
        try {
            return UUID.fromString(value);
        } catch (Exception e) {
            throw new InvalidUniqueId(e);
        }
    }
}
