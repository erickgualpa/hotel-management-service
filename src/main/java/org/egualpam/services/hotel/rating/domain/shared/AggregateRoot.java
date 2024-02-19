package org.egualpam.services.hotel.rating.domain.shared;

import java.util.List;

public interface AggregateRoot {
    List<DomainEvent> getDomainEvents();
}
