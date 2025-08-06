package org.egualpam.contexts.hotel.customer.room.infrastructure.cqrs.query.simple;

import org.egualpam.contexts.hotel.shared.infrastructure.cqrs.query.Query;

public record SyncFindRoomsQuery(String availableFrom, String availableTo) implements Query {}
