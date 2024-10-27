package org.egualpam.contexts.hotelmanagement.review.domain;

import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateId;
import org.egualpam.contexts.hotelmanagement.shared.domain.DomainEvent;

public final class ReviewUpdated extends DomainEvent {

  public ReviewUpdated(AggregateId aggregateId) {
    super(aggregateId);
  }
}
