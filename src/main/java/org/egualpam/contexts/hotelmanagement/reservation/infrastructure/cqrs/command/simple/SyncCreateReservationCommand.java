package org.egualpam.contexts.hotelmanagement.reservation.infrastructure.cqrs.command.simple;

import org.egualpam.contexts.hotel.shared.infrastructure.cqrs.command.Command;

public record SyncCreateReservationCommand(String id, String roomId, String from, String to)
    implements Command {}
