package org.egualpam.contexts.hotelmanagement.review.domain;

import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateId;
import org.egualpam.contexts.hotelmanagement.shared.domain.DomainEvent;

public final class ReviewCreated extends DomainEvent {

  public ReviewCreated(AggregateId aggregateId) {
    super(aggregateId);
  }
}
