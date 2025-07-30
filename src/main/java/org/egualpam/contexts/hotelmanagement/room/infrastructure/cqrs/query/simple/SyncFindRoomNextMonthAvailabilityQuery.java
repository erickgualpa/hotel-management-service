package org.egualpam.contexts.hotelmanagement.room.infrastructure.cqrs.query.simple;

import org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.query.Query;

public record SyncFindRoomNextMonthAvailabilityQuery(String roomId) implements Query {}
