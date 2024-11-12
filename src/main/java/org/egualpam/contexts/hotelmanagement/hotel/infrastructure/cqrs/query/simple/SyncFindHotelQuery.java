package org.egualpam.contexts.hotelmanagement.hotel.infrastructure.cqrs.query.simple;

import org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.query.Query;

public record SyncFindHotelQuery(String hotelId) implements Query {}
