package org.egualpam.contexts.hotelmanagement.shared.domain;

import java.time.Instant;

public interface DomainEvent {
  UniqueId id();

  AggregateId aggregateId();

  Instant occurredOn();
}
