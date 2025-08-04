package org.egualpam.contexts.hotelmanagement.roomprice.application.command;

public record UpdateRoomPriceCommand(String hotelId, String roomType, String priceAmount) {}
