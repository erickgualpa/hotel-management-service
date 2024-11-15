package org.egualpam.contexts.hotelmanagement.hotel.domain;

import java.time.Clock;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateId;
import org.egualpam.contexts.hotelmanagement.shared.domain.DomainEvent;
import org.egualpam.contexts.hotelmanagement.shared.domain.UniqueId;

public class HotelCreatedEvent extends DomainEvent {
  public HotelCreatedEvent(UniqueId id, AggregateId aggregateId, Clock clock) {
    super(id, aggregateId, clock);
  }
}
