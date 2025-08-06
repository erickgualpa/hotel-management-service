package org.egualpam.contexts.hotel.management.roomprice.application.command;

public record UpdateRoomPriceCommand(String hotelId, String roomType, String priceAmount) {}
