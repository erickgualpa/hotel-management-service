package org.egualpam.contexts.hotel.management.hotel.domain;

import org.egualpam.contexts.hotel.shared.domain.AggregateId;

public class HotelNotExists extends RuntimeException {
  public HotelNotExists(AggregateId hotelId) {
    super("No hotel exists with id: [%s]".formatted(hotelId.value()));
  }
}
