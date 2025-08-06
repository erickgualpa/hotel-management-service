package org.egualpam.contexts.hotel.shared.domain;

import java.util.Set;

public interface EventBus {
  void publish(Set<DomainEvent> events);
}
