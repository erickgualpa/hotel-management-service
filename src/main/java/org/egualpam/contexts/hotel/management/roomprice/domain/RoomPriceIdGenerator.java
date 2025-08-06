package org.egualpam.contexts.hotel.management.roomprice.domain;

import org.egualpam.contexts.hotel.shared.domain.AggregateId;
import org.egualpam.contexts.hotel.shared.domain.RoomType;

@FunctionalInterface
public interface RoomPriceIdGenerator {

  AggregateId get(AggregateId hotelId, RoomType roomType);
}
