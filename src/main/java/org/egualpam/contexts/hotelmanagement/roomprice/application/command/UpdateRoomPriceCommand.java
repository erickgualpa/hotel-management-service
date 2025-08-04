package org.egualpam.contexts.hotelmanagement.roomprice.application.command;

public record UpdateRoomPriceCommand(String roomPriceId, String hotelId, String roomType) {}
