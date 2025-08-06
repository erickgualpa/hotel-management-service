package org.egualpam.contexts.hotelmanagement.room.infrastructure.cqrs.command.simple;

import org.egualpam.contexts.hotel.shared.infrastructure.cqrs.command.Command;

public record SyncCreateRoomCommand(String id, String type, String hotelId) implements Command {}
