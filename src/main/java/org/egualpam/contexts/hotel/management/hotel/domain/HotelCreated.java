package org.egualpam.contexts.hotel.management.hotel.domain;

import java.time.Clock;
import org.egualpam.contexts.hotel.shared.domain.AggregateId;
import org.egualpam.contexts.hotel.shared.domain.DomainEvent;
import org.egualpam.contexts.hotel.shared.domain.UniqueId;

public class HotelCreated extends DomainEvent {
  public HotelCreated(UniqueId id, AggregateId aggregateId, Clock clock) {
    super(id, aggregateId, clock);
  }
}
