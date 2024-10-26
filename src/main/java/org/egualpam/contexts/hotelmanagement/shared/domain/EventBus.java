package org.egualpam.contexts.hotelmanagement.shared.domain;

import java.util.List;

public interface EventBus {
  void publish(List<DomainEvent> events);
}
