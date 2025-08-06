package org.egualpam.contexts.hotel.customer.hotel.infrastructure.cqrs.query.simple;

import org.egualpam.contexts.hotel.shared.infrastructure.cqrs.query.Query;

public record SyncFindHotelsQuery(String location, Integer minPrice, Integer maxPrice)
    implements Query {}
