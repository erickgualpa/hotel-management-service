package org.egualpam.contexts.hotel.management.hotel.infrastructure.cqrs.command.simple;

import org.egualpam.contexts.hotel.shared.infrastructure.cqrs.command.Command;

public record AsyncCreateHotelCommand(
    String id, String name, String description, String location, Integer price, String imageURL)
    implements Command {}
