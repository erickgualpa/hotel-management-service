package org.egualpam.contexts.hotel.management.hotel.application.command;

public record CreateHotelCommand(
    String id, String name, String description, String location, Integer price, String imageURL) {}
