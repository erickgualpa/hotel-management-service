package org.egualpam.contexts.hotelmanagement.shared.infrastructure.eventbus.shared.events;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.time.Instant;

@JsonSerialize
public final class HotelRatingUpdatePublicEventV2 implements PublicEvent {

  private final String id;
  private final String type;
  private final String aggregateId;
  private final Instant occurredOn;

  public HotelRatingUpdatePublicEventV2(String id, String aggregateId, Instant occurredOn) {
    this.id = id;
    this.type = "hotelmanagement.hotel-rating.updated";
    this.aggregateId = aggregateId;
    this.occurredOn = occurredOn;
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
}
