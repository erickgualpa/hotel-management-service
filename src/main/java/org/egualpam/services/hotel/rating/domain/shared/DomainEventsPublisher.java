package org.egualpam.services.hotel.rating.domain.shared;

import java.util.List;

public interface DomainEventsPublisher {
    void publish(List<DomainEvent> events);
}
