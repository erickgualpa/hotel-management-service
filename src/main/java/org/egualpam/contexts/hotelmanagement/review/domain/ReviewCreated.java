package org.egualpam.contexts.hotelmanagement.review.domain;

import java.time.Clock;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateId;
import org.egualpam.contexts.hotelmanagement.shared.domain.DomainEvent;
import org.egualpam.contexts.hotelmanagement.shared.domain.UniqueId;

public final class ReviewCreated extends DomainEvent {

  private final HotelId hotelId;
  private final Rating rating;

  public ReviewCreated(
      UniqueId id, AggregateId aggregateId, HotelId hotelId, Rating rating, Clock clock) {
    super(id, aggregateId, clock);
    this.hotelId = hotelId;
    this.rating = rating;
  }

  public HotelId hotelId() {
    return this.hotelId;
  }

  public Rating rating() {
    return this.rating;
  }
}
