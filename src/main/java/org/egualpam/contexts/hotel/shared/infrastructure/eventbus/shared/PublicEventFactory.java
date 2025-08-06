package org.egualpam.contexts.hotel.shared.infrastructure.eventbus.shared;

import org.egualpam.contexts.hotel.customer.review.domain.ReviewCreated;
import org.egualpam.contexts.hotel.customer.review.domain.ReviewUpdated;
import org.egualpam.contexts.hotel.management.hotel.domain.HotelCreated;
import org.egualpam.contexts.hotel.management.hotelrating.domain.HotelRatingInitialized;
import org.egualpam.contexts.hotel.management.hotelrating.domain.HotelRatingUpdated;
import org.egualpam.contexts.hotel.shared.domain.DomainEvent;
import org.egualpam.contexts.hotel.shared.infrastructure.eventbus.shared.events.HotelCreatedPublicEvent;
import org.egualpam.contexts.hotel.shared.infrastructure.eventbus.shared.events.HotelRatingInitializedPublicEvent;
import org.egualpam.contexts.hotel.shared.infrastructure.eventbus.shared.events.HotelRatingUpdatePublicEvent;
import org.egualpam.contexts.hotel.shared.infrastructure.eventbus.shared.events.PublicEvent;
import org.egualpam.contexts.hotel.shared.infrastructure.eventbus.shared.events.ReservationCreatedPublicEvent;
import org.egualpam.contexts.hotel.shared.infrastructure.eventbus.shared.events.ReviewCreatedPublicEvent;
import org.egualpam.contexts.hotel.shared.infrastructure.eventbus.shared.events.ReviewUpdatedPublicEvent;
import org.egualpam.contexts.hotelmanagement.reservation.domain.ReservationCreated;

public final class PublicEventFactory {

  private PublicEventFactory() {}

  public static PublicEvent from(DomainEvent domainEvent) {
    return switch (domainEvent) {
      case ReviewCreated reviewCreated ->
          new ReviewCreatedPublicEvent(
              reviewCreated.id().value(),
              reviewCreated.aggregateId().value(),
              reviewCreated.occurredOn(),
              reviewCreated.hotelId().value(),
              reviewCreated.rating().value());

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

      case HotelRatingInitialized hotelRatingInitialized ->
          new HotelRatingInitializedPublicEvent(
              hotelRatingInitialized.id().value(),
              hotelRatingInitialized.aggregateId().value(),
              hotelRatingInitialized.occurredOn());

      case HotelRatingUpdated hotelRatingUpdated ->
          new HotelRatingUpdatePublicEvent(
              hotelRatingUpdated.id().value(),
              hotelRatingUpdated.aggregateId().value(),
              hotelRatingUpdated.occurredOn());

      case ReservationCreated reservationCreated ->
          new ReservationCreatedPublicEvent(
              reservationCreated.id().value(),
              reservationCreated.aggregateId().value(),
              reservationCreated.occurredOn());

      default -> throw new UnsupportedDomainEvent(domainEvent);
    };
  }
}
