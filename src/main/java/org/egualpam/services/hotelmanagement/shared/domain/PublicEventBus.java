package org.egualpam.services.hotelmanagement.shared.domain;

import java.util.List;

public interface PublicEventBus {
    void publish(List<DomainEvent> events);
}
