package org.egualpam.contexts.hotelmanagement.review.application.command;

import java.time.Clock;
import org.egualpam.contexts.hotelmanagement.review.domain.HotelId;
import org.egualpam.contexts.hotelmanagement.review.domain.Rating;
import org.egualpam.contexts.hotelmanagement.shared.application.command.InternalEvent;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateId;
import org.egualpam.contexts.hotelmanagement.shared.domain.UniqueId;

public final class ReviewCreated extends InternalEvent {

  private final HotelId hotelId;
  private final Rating rating;

  public ReviewCreated(
      UniqueId id, AggregateId aggregateId, Clock clock, HotelId hotelId, Rating rating) {
    super(id, aggregateId, clock);
    this.hotelId = hotelId;
    this.rating = rating;
  }

  public HotelId hotelId() {
    return hotelId;
  }

  public Rating rating() {
    return rating;
  }
}
