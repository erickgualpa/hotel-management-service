package org.egualpam.contexts.hotelmanagement.shared.domain;

import java.util.List;

public interface AggregateRoot {
    AggregateId getId();

    List<DomainEvent> pullDomainEvents();
}
