package org.egualpam.contexts.hotelmanagement.reservation.infrastructure.cqrs.command.simple;

import org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.command.Command;

public record SyncCreateReservationCommand(String id, String roomType, String from, String to)
    implements Command {}
