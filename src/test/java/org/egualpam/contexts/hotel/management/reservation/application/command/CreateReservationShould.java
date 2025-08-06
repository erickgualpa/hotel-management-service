package org.egualpam.contexts.hotel.management.reservation.application.command;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import org.egualpam.contexts.hotel.management.reservation.domain.Reservation;
import org.egualpam.contexts.hotel.management.reservation.domain.ReservationCreated;
import org.egualpam.contexts.hotel.shared.domain.AggregateId;
import org.egualpam.contexts.hotel.shared.domain.AggregateRepository;
import org.egualpam.contexts.hotel.shared.domain.DateRange;
import org.egualpam.contexts.hotel.shared.domain.DomainEvent;
import org.egualpam.contexts.hotel.shared.domain.EventBus;
import org.egualpam.contexts.hotel.shared.domain.UniqueId;
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
  @Mock private Supplier<UniqueId> uniqueIdSupplier;
  @Mock private AggregateRepository<Reservation> repository;
  @Captor private ArgumentCaptor<Set<DomainEvent>> domainEventsCaptor;
  @Mock private EventBus eventBus;
  @Mock private Clock clock;

  private CreateReservation testee;

  @BeforeEach
  void setUp() {
    testee = new CreateReservation(uniqueIdSupplier, repository, eventBus, clock);
  }

  @Test
  void createReservation() {
    String reservationId = randomUUID().toString();
    String roomId = randomUUID().toString();
    String reservedFrom = "2025-11-25";
    String reservedTo = "2025-11-27";

    String domainEventId = randomUUID().toString();
    Instant occurredOn = Instant.now();

    when(uniqueIdSupplier.get()).thenReturn(new UniqueId(domainEventId));
    when(clock.instant()).thenReturn(occurredOn);

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

    verify(eventBus).publish(domainEventsCaptor.capture());
    assertThat(domainEventsCaptor.getValue())
        .hasSize(1)
        .first()
        .isInstanceOf(ReservationCreated.class)
        .satisfies(
            domainEvent -> {
              assertThat(domainEvent.id().value()).isEqualTo(domainEventId);
              assertThat(domainEvent.aggregateId().value()).isEqualTo(reservationId);
              assertThat(domainEvent.occurredOn()).isEqualTo(occurredOn);

              ReservationCreated reservationCreated = (ReservationCreated) domainEvent;
              assertThat(reservationCreated.roomId()).isEqualTo(roomId);
              assertThat(reservationCreated.reservationDateRange())
                  .isEqualTo(new DateRange(reservedFrom, reservedTo));
            });
  }

  @Test
  void notCreateReservationWhenAlreadyExists() {
    String reservationId = randomUUID().toString();
    String roomId = randomUUID().toString();
    String reservedFrom = "2025-11-25";
    String reservedTo = "2025-11-27";

    Reservation existing = Reservation.load(reservationId, roomId, reservedFrom, reservedTo);
    when(repository.find(new AggregateId(reservationId))).thenReturn(Optional.of(existing));

    CreateReservationCommand command =
        new CreateReservationCommand(reservationId, roomId, reservedFrom, reservedTo);

    testee.execute(command);

    verify(repository, never()).save(any(Reservation.class));
    verify(eventBus, never()).publish(anySet());
  }
}
