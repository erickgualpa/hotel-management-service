package org.egualpam.contexts.hotelmanagement.hotel.domain;

import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateId;
import org.egualpam.contexts.hotelmanagement.shared.domain.DomainEvent;

public class HotelCreatedEvent extends DomainEvent {
  public HotelCreatedEvent(AggregateId aggregateId) {
    super(aggregateId);
  }
}
