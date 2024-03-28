package org.egualpam.services.hotelmanagement.domain.shared;

import java.util.List;

public interface PublicEventBus {
    void publish(List<DomainEvent> events);
}
