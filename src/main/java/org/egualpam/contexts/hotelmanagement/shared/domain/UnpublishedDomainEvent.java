package org.egualpam.contexts.hotelmanagement.shared.domain;

import org.egualpam.contexts.hotelmanagement.shared.infrastructure.eventbus.events.PublicEvent;

public class UnpublishedDomainEvent extends RuntimeException {
  public UnpublishedDomainEvent(PublicEvent event, Throwable cause) {
    super(
        "Domain event [%s]:[%s] for entity [%s] was not published"
            .formatted(event.getType(), event.getVersion(), event.getAggregateId()),
        cause);
  }
}
