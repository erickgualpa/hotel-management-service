package org.egualpam.contexts.hotelmanagement.shared.domain;

public class UnpublishedDomainEvent extends RuntimeException {
  public UnpublishedDomainEvent(DomainEvent event, Throwable cause) {
    super(
        "Domain event [%s] for the entity [%s] was not published"
            .formatted(event.getClass().getName(), event.aggregateId().value()),
        cause);
  }
}
