package org.egualpam.services.hotel.rating.domain.shared;

public interface DomainEvent {
    AggregateId getAggregateId();

    String getType();
}
