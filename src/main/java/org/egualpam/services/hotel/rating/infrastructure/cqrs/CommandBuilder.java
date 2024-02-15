package org.egualpam.services.hotel.rating.infrastructure.cqrs;

import org.egualpam.services.hotel.rating.application.shared.Command;

public interface CommandBuilder {
    Command createReview(
            String reviewIdentifier,
            String hotelIdentifier,
            Integer rating,
            String comment
    );
}
