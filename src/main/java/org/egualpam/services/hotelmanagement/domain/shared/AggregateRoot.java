package org.egualpam.services.hotelmanagement.domain.shared;

import java.util.List;

public interface AggregateRoot {
    AggregateId getId();

    List<DomainEvent> pullDomainEvents();
}
