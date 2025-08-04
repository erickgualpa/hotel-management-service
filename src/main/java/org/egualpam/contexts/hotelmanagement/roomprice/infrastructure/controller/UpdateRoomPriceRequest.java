package org.egualpam.contexts.hotelmanagement.roomprice.infrastructure.controller;

public record UpdateRoomPriceRequest(
    String id, String hotelId, String roomType, String priceAmount) {}
