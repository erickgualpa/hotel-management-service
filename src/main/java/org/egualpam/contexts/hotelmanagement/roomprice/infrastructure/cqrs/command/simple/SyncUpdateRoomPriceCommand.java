package org.egualpam.contexts.hotelmanagement.roomprice.infrastructure.cqrs.command.simple;

import org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.command.Command;

public record SyncUpdateRoomPriceCommand(String roomPriceId, String hotelId, String roomType)
    implements Command {}
