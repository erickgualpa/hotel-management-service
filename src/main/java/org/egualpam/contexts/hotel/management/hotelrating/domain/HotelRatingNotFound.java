package org.egualpam.contexts.hotel.management.hotelrating.domain;

import org.egualpam.contexts.hotel.shared.domain.AggregateId;

public class HotelRatingNotFound extends RuntimeException {
  public HotelRatingNotFound(AggregateId id) {
    super("No hotel rating found with id: [%s]".formatted(id.value()));
  }
}
