package org.egualpam.contexts.hotelmanagement.shared.infrastructure.eventbus.events;

import org.egualpam.contexts.hotelmanagement.review.domain.ReviewCreated;
import org.egualpam.contexts.hotelmanagement.review.domain.ReviewUpdated;
import org.egualpam.contexts.hotelmanagement.shared.domain.DomainEvent;

public final class PublicEventFactory {

  private PublicEventFactory() {}

  public static PublicEvent from(DomainEvent domainEvent) {
    if (domainEvent instanceof ReviewCreated) {
      return new ReviewCreatedPublicEvent(
          domainEvent.id().value(), domainEvent.aggregateId().value(), domainEvent.occurredOn());
    } else if (domainEvent instanceof ReviewUpdated) {
      return new ReviewUpdatedPublicEvent(
          domainEvent.id().toString(), domainEvent.aggregateId().value(), domainEvent.occurredOn());
    } else {
      throw new UnsupportedDomainEvent(domainEvent);
    }
  }
}
