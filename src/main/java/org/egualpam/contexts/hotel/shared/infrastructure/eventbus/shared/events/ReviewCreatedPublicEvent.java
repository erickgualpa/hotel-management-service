package org.egualpam.contexts.hotel.shared.infrastructure.eventbus.shared.events;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.time.Instant;

@JsonSerialize
public final class ReviewCreatedPublicEvent implements PublicEvent {

  private final String id;
  private final String type;
  private final String aggregateId;
  private final Instant occurredOn;

  private final String hotelId;
  private final Integer reviewRating;

  public ReviewCreatedPublicEvent(
      String id, String aggregateId, Instant occurredOn, String hotelId, Integer reviewRating) {
    this.id = id;
    this.type = "hotelmanagement.review.created";
    this.aggregateId = aggregateId;
    this.occurredOn = occurredOn;
    this.hotelId = hotelId;
    this.reviewRating = reviewRating;
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public String getType() {
    return type;
  }

  @Override
  public String getVersion() {
    return "1.0";
  }

  @Override
  public String getAggregateId() {
    return aggregateId;
  }

  @Override
  public Instant getOccurredOn() {
    return occurredOn;
  }

  public String getHotelId() {
    return hotelId;
  }

  public Integer getReviewRating() {
    return reviewRating;
  }
}
