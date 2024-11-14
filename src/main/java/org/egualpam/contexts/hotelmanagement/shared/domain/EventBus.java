package org.egualpam.contexts.hotelmanagement.shared.domain;

import java.util.Set;

public interface EventBus {
  void publish(Set<DomainEvent> events);
}
