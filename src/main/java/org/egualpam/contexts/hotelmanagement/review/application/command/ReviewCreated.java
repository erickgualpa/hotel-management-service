package org.egualpam.contexts.hotelmanagement.review.application.command;

import org.egualpam.contexts.hotelmanagement.review.domain.HotelId;
import org.egualpam.contexts.hotelmanagement.review.domain.Rating;
import org.egualpam.contexts.hotelmanagement.shared.application.command.InternalEvent;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateId;

public final class ReviewCreated extends InternalEvent {

  private final HotelId hotelId;
  private final Rating rating;

  public ReviewCreated(AggregateId aggregateId, HotelId hotelId, Rating rating) {
    super(aggregateId);
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
