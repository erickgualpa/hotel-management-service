package org.egualpam.contexts.hotelmanagement.hotelrating.infrastructure.cqrs.command.simple;

import org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.command.Command;

public record SyncUpdateHotelRatingCommand(String hotelId, String reviewId, Integer reviewRating)
    implements Command {}
