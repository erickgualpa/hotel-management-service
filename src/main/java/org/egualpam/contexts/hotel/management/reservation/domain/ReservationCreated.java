package org.egualpam.contexts.hotel.management.reservation.domain;

import java.time.Clock;
import org.egualpam.contexts.hotel.shared.domain.AggregateId;
import org.egualpam.contexts.hotel.shared.domain.DateRange;
import org.egualpam.contexts.hotel.shared.domain.DomainEvent;
import org.egualpam.contexts.hotel.shared.domain.UniqueId;

public class ReservationCreated extends DomainEvent {

  private final AggregateId roomId;
  private final DateRange reservationDateRange;

  ReservationCreated(
      UniqueId id,
      AggregateId aggregateId,
      Clock clock,
      AggregateId roomId,
      DateRange reservationDateRange) {
    super(id, aggregateId, clock);
    this.roomId = roomId;
    this.reservationDateRange = reservationDateRange;
  }

  public String roomId() {
    return this.roomId.value();
  }

  public DateRange reservationDateRange() {
    return this.reservationDateRange;
  }
}
