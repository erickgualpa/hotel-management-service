package org.egualpam.contexts.hotelmanagement.shared.infrastructure.eventbus.events;

import org.egualpam.contexts.hotelmanagement.hotel.domain.HotelCreated;
import org.egualpam.contexts.hotelmanagement.review.domain.ReviewCreated;
import org.egualpam.contexts.hotelmanagement.review.domain.ReviewUpdated;
import org.egualpam.contexts.hotelmanagement.shared.domain.DomainEvent;

public final class PublicEventFactory {

  private PublicEventFactory() {}

  public static PublicEvent from(DomainEvent domainEvent) {
    return switch (domainEvent) {
      case ReviewCreated reviewCreated ->
          new ReviewCreatedPublicEvent(
              reviewCreated.id().value(),
              reviewCreated.aggregateId().value(),
              reviewCreated.occurredOn());
      case ReviewUpdated reviewUpdated ->
          new ReviewUpdatedPublicEvent(
              reviewUpdated.id().value(),
              reviewUpdated.aggregateId().value(),
              reviewUpdated.occurredOn());
      case HotelCreated hotelCreated ->
          new HotelCreatedPublicEvent(
              hotelCreated.id().value(),
              hotelCreated.aggregateId().value(),
              hotelCreated.occurredOn());
      default -> throw new UnsupportedDomainEvent(domainEvent);
    };
  }
}
