package org.egualpam.contexts.hotel.management.reservation.domain;

import static java.util.Objects.isNull;

import java.time.Clock;
import java.util.function.Supplier;
import org.egualpam.contexts.hotel.shared.domain.AggregateId;
import org.egualpam.contexts.hotel.shared.domain.AggregateRepository;
import org.egualpam.contexts.hotel.shared.domain.AggregateRoot;
import org.egualpam.contexts.hotel.shared.domain.DateRange;
import org.egualpam.contexts.hotel.shared.domain.RequiredPropertyIsMissing;
import org.egualpam.contexts.hotel.shared.domain.UniqueId;

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

  public static Reservation load(
      String reservationId, String roomId, String reservedFrom, String reservedTo) {
    return new Reservation(reservationId, roomId, reservedFrom, reservedTo);
  }

  public static Reservation create(
      AggregateRepository<Reservation> repository,
      Supplier<UniqueId> uniqueIdSupplier,
      Clock clock,
      String reservationId,
      String roomId,
      String reservedFrom,
      String reservedTo) {
    final var aggregateId = new AggregateId(reservationId);

    final var existing = repository.find(aggregateId);

    if (existing.isPresent()) {
      throw new ReservationAlreadyExists();
    }

    final var reservation = new Reservation(reservationId, roomId, reservedFrom, reservedTo);

    final var reservationCreated =
        new ReservationCreated(
            uniqueIdSupplier.get(),
            aggregateId,
            clock,
            reservation.roomId,
            reservation.reservationDateRange);

    reservation.addDomainEvent(reservationCreated);

    return reservation;
  }

  public String roomId() {
    return this.roomId.value();
  }

  public DateRange reservationDateRange() {
    return this.reservationDateRange;
  }
}
