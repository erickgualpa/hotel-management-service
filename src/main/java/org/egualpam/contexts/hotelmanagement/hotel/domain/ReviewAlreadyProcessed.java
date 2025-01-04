package org.egualpam.contexts.hotelmanagement.hotel.domain;

import org.egualpam.contexts.hotelmanagement.shared.domain.EntityId;

public class ReviewAlreadyProcessed extends RuntimeException {
  public ReviewAlreadyProcessed(EntityId reviewId) {
    super("Review with id [%s] was already processed".formatted(reviewId.value()));
  }
}
