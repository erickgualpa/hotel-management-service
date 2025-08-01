package org.egualpam.contexts.hotelmanagement.reservation.application.command;

import org.egualpam.contexts.hotelmanagement.reservation.domain.Reservation;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateRepository;

public class CreateReservation {

  private final AggregateRepository<Reservation> repository;

  public CreateReservation(AggregateRepository<Reservation> repository) {
    this.repository = repository;
  }

  public void execute(CreateReservationCommand command) {
    Reservation reservation =
        Reservation.create(
            command.reservationId(),
            command.roomId(),
            command.reservedFrom(),
            command.reservedTo());

    repository.save(reservation);
  }
}
