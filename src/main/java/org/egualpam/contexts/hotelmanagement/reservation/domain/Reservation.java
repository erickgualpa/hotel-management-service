package org.egualpam.contexts.hotelmanagement.reservation.domain;

import static java.util.Objects.isNull;

import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateId;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateRoot;
import org.egualpam.contexts.hotelmanagement.shared.domain.DateRange;
import org.egualpam.contexts.hotelmanagement.shared.domain.RequiredPropertyIsMissing;

public class Reservation extends AggregateRoot {

  private final AggregateId roomId;
  private final DateRange reservationDateRange;

  private Reservation(String id, String roomId, String reservedFrom, String reservedTo) {
    super(id);
    if (isNull(roomId) || isNull(reservedFrom) || isNull(reservedTo)) {
      throw new RequiredPropertyIsMissing();
    }
    this.roomId = new AggregateId(roomId);
    this.reservationDateRange = new DateRange(reservedFrom, reservedTo);
  }

  public static Reservation create(
      String reservationId, String roomId, String reservedFrom, String reservedTo) {
    return new Reservation(reservationId, roomId, reservedFrom, reservedTo);
  }

  public String roomId() {
    return this.roomId.value();
  }

  public DateRange reservationDateRange() {
    return this.reservationDateRange;
  }
}
