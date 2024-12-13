package org.egualpam.contexts.hotelmanagement.hotel.domain;

import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateId;

public class HotelNotExists extends RuntimeException {
  public HotelNotExists(AggregateId hotelId) {
    super("No hotel exists with id: [%s]".formatted(hotelId.value()));
  }
}
