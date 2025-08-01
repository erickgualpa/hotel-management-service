package org.egualpam.contexts.hotelmanagement.reservation.application.command;

public record CreateReservationCommand(
    String reservationId, String roomType, String reservedFrom, String reservedTo) {}
