package org.egualpam.services.hotelmanagement.domain.shared;

import java.util.List;

public interface DomainEventsPublisher {
    void publish(List<DomainEvent> events);
}
