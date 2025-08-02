package org.egualpam.contexts.hotelmanagement.reservation.application.command;

import org.egualpam.contexts.hotelmanagement.reservation.domain.Reservation;
import org.egualpam.contexts.hotelmanagement.reservation.domain.ReservationAlreadyExists;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateRepository;

public class CreateReservation {

  private final AggregateRepository<Reservation> repository;

  public CreateReservation(AggregateRepository<Reservation> repository) {
    this.repository = repository;
  }

  public void execute(CreateReservationCommand command) {
    final Reservation reservation;

    try {
      reservation =
          Reservation.create(
              repository,
              command.reservationId(),
              command.roomId(),
              command.reservedFrom(),
              command.reservedTo());
    } catch (ReservationAlreadyExists e) {
      return;
    }

    repository.save(reservation);
  }
}
