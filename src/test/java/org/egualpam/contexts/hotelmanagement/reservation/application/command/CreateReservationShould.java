package org.egualpam.contexts.hotelmanagement.reservation.application.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import java.util.UUID;
import org.egualpam.contexts.hotelmanagement.reservation.domain.Reservation;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateRepository;
import org.egualpam.contexts.hotelmanagement.shared.domain.DateRange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateReservationShould {

  @Captor private ArgumentCaptor<Reservation> reservationCaptor;
  @Mock private AggregateRepository<Reservation> repository;

  private CreateReservation testee;

  @BeforeEach
  void setUp() {
    testee = new CreateReservation(repository);
  }

  @Test
  void createReservation() {
    String reservationId = UUID.randomUUID().toString();
    String roomId = UUID.randomUUID().toString();
    String reservedFrom = "2025-11-25";
    String reservedTo = "2025-11-27";

    CreateReservationCommand command =
        new CreateReservationCommand(reservationId, roomId, reservedFrom, reservedTo);

    testee.execute(command);

    verify(repository).save(reservationCaptor.capture());
    assertThat(reservationCaptor.getValue())
        .satisfies(
            saved -> {
              assertThat(saved.id().value()).isEqualTo(reservationId);
              assertThat(saved.roomId()).isEqualTo(roomId);
              assertThat(saved.reservationDateRange())
                  .isEqualTo(new DateRange(reservedFrom, reservedTo));
            });
  }
}
