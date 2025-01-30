package org.egualpam.contexts.hotelmanagement.hotelrating.domain;

import java.time.Clock;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateId;
import org.egualpam.contexts.hotelmanagement.shared.domain.DomainEvent;
import org.egualpam.contexts.hotelmanagement.shared.domain.UniqueId;

public final class HotelRatingUpdated extends DomainEvent {

  public HotelRatingUpdated(UniqueId id, AggregateId aggregateId, Clock clock) {
    super(id, aggregateId, clock);
  }
}
