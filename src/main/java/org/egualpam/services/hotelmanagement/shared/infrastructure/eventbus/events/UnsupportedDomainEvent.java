package org.egualpam.services.hotelmanagement.shared.infrastructure.eventbus.events;

import org.egualpam.services.hotelmanagement.shared.domain.DomainEvent;

public final class UnsupportedDomainEvent extends RuntimeException {
    public UnsupportedDomainEvent(DomainEvent domainEvent) {
        super("Unsupported domain event: " + domainEvent.getClass().getName());
    }
}
