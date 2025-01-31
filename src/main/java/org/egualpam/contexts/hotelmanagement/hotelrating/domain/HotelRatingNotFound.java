package org.egualpam.contexts.hotelmanagement.hotelrating.domain;

import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateId;

public class HotelRatingNotFound extends RuntimeException {
  public HotelRatingNotFound(AggregateId id) {
    super("No hotel rating found with id: [%s]".formatted(id.value()));
  }
}
