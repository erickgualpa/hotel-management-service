package org.egualpam.contexts.hotelmanagement.reservation.infrastructure.controller;

public record PostReservationRequest(String id, String roomType, String from, String to) {}
