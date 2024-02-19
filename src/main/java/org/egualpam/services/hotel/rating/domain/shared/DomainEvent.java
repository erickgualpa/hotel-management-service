package org.egualpam.services.hotel.rating.domain.shared;

public interface DomainEvent {
    Identifier getAggregateId();

    String getType();
}
