package org.egualpam.contexts.hotelmanagement.hotel.infrastructure.cqrs.query.simple;

import org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.query.Query;

public record SyncFindHotelsQuery(String location, Integer minPrice, Integer maxPrice)
    implements Query {}
