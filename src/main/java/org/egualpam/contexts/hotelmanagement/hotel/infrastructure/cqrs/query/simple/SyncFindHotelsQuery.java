package org.egualpam.contexts.hotelmanagement.hotel.infrastructure.cqrs.query.simple;

import org.egualpam.contexts.hotelmanagement.shared.application.query.Query;

public record SyncFindHotelsQuery(String location, Integer minPrice, Integer maxPrice)
    implements Query {}
