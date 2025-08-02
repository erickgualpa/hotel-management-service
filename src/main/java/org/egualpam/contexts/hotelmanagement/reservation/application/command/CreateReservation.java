package org.egualpam.contexts.hotelmanagement.reservation.application.command;

import java.time.Clock;
import java.util.function.Supplier;
import org.egualpam.contexts.hotelmanagement.reservation.domain.Reservation;
import org.egualpam.contexts.hotelmanagement.reservation.domain.ReservationAlreadyExists;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateRepository;
import org.egualpam.contexts.hotelmanagement.shared.domain.EventBus;
import org.egualpam.contexts.hotelmanagement.shared.domain.UniqueId;

public class CreateReservation {

  private final Supplier<UniqueId> uniqueIdSupplier;
  private final AggregateRepository<Reservation> repository;
  private final EventBus eventBus;
  private final Clock clock;

  public CreateReservation(
      Supplier<UniqueId> uniqueIdSupplier,
      AggregateRepository<Reservation> repository,
      EventBus eventBus,
      Clock clock) {
    this.uniqueIdSupplier = uniqueIdSupplier;
    this.repository = repository;
    this.eventBus = eventBus;
    this.clock = clock;
  }

  public void execute(CreateReservationCommand command) {
    final Reservation reservation;

    try {
      reservation =
          Reservation.create(
              repository,
              uniqueIdSupplier,
              clock,
              command.reservationId(),
              command.roomId(),
              command.reservedFrom(),
              command.reservedTo());
    } catch (ReservationAlreadyExists e) {
      return;
    }

    repository.save(reservation);
    eventBus.publish(reservation.pullDomainEvents());
  }
}
