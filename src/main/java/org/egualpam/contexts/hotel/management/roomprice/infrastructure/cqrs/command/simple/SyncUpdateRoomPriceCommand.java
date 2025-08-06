package org.egualpam.contexts.hotel.management.roomprice.infrastructure.cqrs.command.simple;

import org.egualpam.contexts.hotel.shared.infrastructure.cqrs.command.Command;

public record SyncUpdateRoomPriceCommand(String hotelId, String roomType, String priceAmount)
    implements Command {}
