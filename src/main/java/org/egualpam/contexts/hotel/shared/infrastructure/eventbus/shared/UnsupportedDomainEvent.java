package org.egualpam.contexts.hotel.shared.infrastructure.eventbus.shared;

import org.egualpam.contexts.hotel.shared.domain.DomainEvent;

public final class UnsupportedDomainEvent extends RuntimeException {
  public UnsupportedDomainEvent(DomainEvent domainEvent) {
    super("Unsupported domain event: " + domainEvent.getClass().getName());
  }
}
