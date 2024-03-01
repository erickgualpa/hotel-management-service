package org.egualpam.services.hotel.rating.domain.shared;

import java.util.List;

public interface AggregateRoot {
    AggregateId getId();

    List<DomainEvent> pullDomainEvents();
}
