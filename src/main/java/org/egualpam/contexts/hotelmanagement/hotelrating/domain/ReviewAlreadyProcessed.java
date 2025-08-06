package org.egualpam.contexts.hotelmanagement.hotelrating.domain;

import org.egualpam.contexts.hotel.shared.domain.AggregateId;

public class ReviewAlreadyProcessed extends RuntimeException {
  public ReviewAlreadyProcessed(AggregateId reviewId) {
    super("Review with id [%s] was already processed".formatted(reviewId.value()));
  }
}
