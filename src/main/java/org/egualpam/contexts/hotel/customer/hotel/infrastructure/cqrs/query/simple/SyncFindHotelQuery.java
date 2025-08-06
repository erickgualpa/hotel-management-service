package org.egualpam.contexts.hotel.customer.hotel.infrastructure.cqrs.query.simple;

import org.egualpam.contexts.hotel.shared.infrastructure.cqrs.query.Query;

public record SyncFindHotelQuery(String hotelId) implements Query {}
