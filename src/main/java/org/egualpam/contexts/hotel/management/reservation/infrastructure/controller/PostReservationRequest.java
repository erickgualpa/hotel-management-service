package org.egualpam.contexts.hotel.management.reservation.infrastructure.controller;

public record PostReservationRequest(String id, String roomId, String from, String to) {}
