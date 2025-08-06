package org.egualpam.contexts.hotel.management.hotel.domain;

import org.egualpam.contexts.hotel.shared.domain.AggregateId;

public class HotelAlreadyExists extends RuntimeException {
  public HotelAlreadyExists(AggregateId hotelId) {
    super("Hotel with id [%s] already exists".formatted(hotelId.value()));
  }
}
