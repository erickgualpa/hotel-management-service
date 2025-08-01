package org.egualpam.contexts.hotelmanagement.reservation.infrastructure.controller;

public record PostReservationRequest(String id, String roomId, String from, String to) {}
