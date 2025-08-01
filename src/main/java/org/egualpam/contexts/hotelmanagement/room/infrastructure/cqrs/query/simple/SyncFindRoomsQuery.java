package org.egualpam.contexts.hotelmanagement.room.infrastructure.cqrs.query.simple;

import org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.query.Query;

public record SyncFindRoomsQuery(String availableFrom, String availableTo) implements Query {}
