package org.egualpam.services.hotelmanagement.domain.shared;

import java.util.List;

public interface DomainEventsBus {
    void publish(List<DomainEvent> events);
}
