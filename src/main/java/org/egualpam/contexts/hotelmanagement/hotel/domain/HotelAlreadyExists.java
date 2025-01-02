package org.egualpam.contexts.hotelmanagement.hotel.domain;

import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateId;

public class HotelAlreadyExists extends RuntimeException {
  public HotelAlreadyExists(AggregateId hotelId) {
    super("Hotel with id [%s] already exists".formatted(hotelId.value()));
  }
}
