package org.egualpam.services.hotelmanagement.shared.domain;

import java.util.List;

public interface AggregateRoot {
    AggregateId getId();

    List<DomainEvent> pullDomainEvents();
}
