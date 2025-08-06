package org.egualpam.contexts.hotel.management.hotelrating.domain;

import org.egualpam.contexts.hotel.shared.domain.AggregateId;

public class HotelRatingAlreadyExists extends RuntimeException {
  public HotelRatingAlreadyExists(AggregateId id) {
    super("Hotel rating with id [%s] already exists".formatted(id.value()));
  }
}
