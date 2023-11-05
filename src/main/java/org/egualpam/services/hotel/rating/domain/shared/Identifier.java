package org.egualpam.services.hotel.rating.domain.shared;

import static java.util.UUID.fromString;

public record Identifier(String value) {
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public Identifier {
        try {
            fromString(value);
        } catch (IllegalArgumentException e) {
            throw new InvalidIdentifier();
        }
    }
}
