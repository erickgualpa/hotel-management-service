package org.egualpam.contexts.hotelmanagement.hotel.domain;

import java.time.Clock;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateId;
import org.egualpam.contexts.hotelmanagement.shared.domain.DomainEvent;
import org.egualpam.contexts.hotelmanagement.shared.domain.EntityId;
import org.egualpam.contexts.hotelmanagement.shared.domain.UniqueId;

public final class HotelRatingUpdated extends DomainEvent {

  private final EntityId reviewId;

  public HotelRatingUpdated(UniqueId id, AggregateId aggregateId, EntityId reviewId, Clock clock) {
    super(id, aggregateId, clock);
    this.reviewId = reviewId;
  }

  public EntityId reviewId() {
    return this.reviewId;
  }
}
