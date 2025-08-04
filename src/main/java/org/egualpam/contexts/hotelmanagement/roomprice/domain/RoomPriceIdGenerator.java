package org.egualpam.contexts.hotelmanagement.roomprice.domain;

import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateId;
import org.egualpam.contexts.hotelmanagement.shared.domain.RoomType;

@FunctionalInterface
public interface RoomPriceIdGenerator {

  String get(AggregateId hotelId, RoomType roomType);
}
