package org.egualpam.contexts.hotelmanagement.review.domain;

import java.time.Clock;
import org.egualpam.contexts.hotel.shared.domain.AggregateId;
import org.egualpam.contexts.hotel.shared.domain.DomainEvent;
import org.egualpam.contexts.hotel.shared.domain.UniqueId;

public final class ReviewUpdated extends DomainEvent {
  public ReviewUpdated(UniqueId id, AggregateId aggregateId, Clock clock) {
    super(id, aggregateId, clock);
  }
}
