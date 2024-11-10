package org.egualpam.contexts.hotelmanagement.hotel.application.command;

import org.egualpam.contexts.hotelmanagement.shared.application.command.Command;

public record CreateHotelCommand(
    String id, String name, String description, String location, Integer price, String imageURL)
    implements Command {}
