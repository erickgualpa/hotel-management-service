package org.egualpam.contexts.hotelmanagement.hotelrating.domain;

import org.egualpam.contexts.hotelmanagement.shared.domain.UniqueId;

public class ReviewAlreadyProcessed extends RuntimeException {
  public ReviewAlreadyProcessed(UniqueId reviewId) {
    super("Review with id [%s] was already processed".formatted(reviewId.value()));
  }
}
