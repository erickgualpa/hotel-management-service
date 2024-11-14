package org.egualpam.contexts.hotelmanagement.hotel.infrastructure.cqrs.command.simple;

import org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.command.Command;

public record AsyncCreateHotelCommand(
    String id, String name, String description, String location, Integer price, String imageURL)
    implements Command {}
