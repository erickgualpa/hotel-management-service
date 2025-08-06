package org.egualpam.contexts.hotelmanagement.hotelrating.infrastructure.cqrs.command.simple;

import org.egualpam.contexts.hotel.shared.infrastructure.cqrs.command.Command;

public record SyncInitializeHotelRatingCommand(String hotelId) implements Command {}
