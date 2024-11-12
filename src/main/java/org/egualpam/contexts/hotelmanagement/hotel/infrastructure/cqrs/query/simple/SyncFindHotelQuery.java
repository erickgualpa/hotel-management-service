package org.egualpam.contexts.hotelmanagement.hotel.infrastructure.cqrs.query.simple;

import org.egualpam.contexts.hotelmanagement.shared.application.query.Query;

public record SyncFindHotelQuery(String hotelId) implements Query {}
