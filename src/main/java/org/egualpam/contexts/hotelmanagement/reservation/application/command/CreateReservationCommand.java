package org.egualpam.contexts.hotelmanagement.reservation.application.command;

public record CreateReservationCommand(
    String reservationId, String roomId, String reservedFrom, String reservedTo) {}
