package org.egualpam.contexts.hotelmanagement.reservation.application.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;
import org.egualpam.contexts.hotelmanagement.reservation.domain.Reservation;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateId;
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

  @Test
  void notCreateReservationWhenAlreadyExists() {
    String reservationId = UUID.randomUUID().toString();
    String roomId = UUID.randomUUID().toString();
    String reservedFrom = "2025-11-25";
    String reservedTo = "2025-11-27";

    Reservation existing = Reservation.load(reservationId, roomId, reservedFrom, reservedTo);
    when(repository.find(new AggregateId(reservationId))).thenReturn(Optional.of(existing));

    CreateReservationCommand command =
        new CreateReservationCommand(reservationId, roomId, reservedFrom, reservedTo);

    testee.execute(command);

    verify(repository, never()).save(any(Reservation.class));
  }
}
