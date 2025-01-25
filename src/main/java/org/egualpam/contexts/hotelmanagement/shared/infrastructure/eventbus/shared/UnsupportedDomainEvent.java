package org.egualpam.contexts.hotelmanagement.shared.infrastructure.eventbus.shared;

import org.egualpam.contexts.hotelmanagement.shared.domain.DomainEvent;

public final class UnsupportedDomainEvent extends RuntimeException {
  public UnsupportedDomainEvent(DomainEvent domainEvent) {
    super("Unsupported domain event: " + domainEvent.getClass().getName());
  }
}
