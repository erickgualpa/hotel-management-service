package org.egualpam.contexts.hotel.management.reservation.application.command;

public record CreateReservationCommand(
    String reservationId, String roomId, String reservedFrom, String reservedTo) {}
