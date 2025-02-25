package org.egualpam.contexts.hotelmanagement.hotel.application.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

import java.time.Clock;
import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import org.egualpam.contexts.hotelmanagement.hotel.domain.Hotel;
import org.egualpam.contexts.hotelmanagement.hotel.domain.HotelCreated;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateId;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateRepository;
import org.egualpam.contexts.hotelmanagement.shared.domain.DomainEvent;
import org.egualpam.contexts.hotelmanagement.shared.domain.EventBus;
import org.egualpam.contexts.hotelmanagement.shared.domain.UniqueId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateHotelShould {

  @Mock private Clock clock;
  @Mock private Supplier<UniqueId> uniqueIdSupplier;
  @Mock private AggregateRepository<Hotel> repository;
  @Mock private EventBus eventBus;

  @Captor private ArgumentCaptor<Hotel> hotelCaptor;
  @Captor private ArgumentCaptor<Set<DomainEvent>> domainEventsCaptor;

  private static final Instant NOW = Instant.now();

  private CreateHotel testee;

  @BeforeEach
  void setUp() {
    testee = new CreateHotel(clock, uniqueIdSupplier, repository, eventBus);
  }

  @Test
  void createHotel() {
    String id = UniqueId.get().value();
    String name = randomAlphabetic(5);
    String description = randomAlphabetic(10);
    String location = randomAlphabetic(5);
    Integer price = 100;
    String imageURL = "www." + randomAlphabetic(5) + ".com";
    UniqueId domainEventId = UniqueId.get();

    when(clock.instant()).thenReturn(NOW);
    when(uniqueIdSupplier.get()).thenReturn(domainEventId);

    CreateHotelCommand command =
        new CreateHotelCommand(id, name, description, location, price, imageURL);
    testee.execute(command);

    verify(repository).save(hotelCaptor.capture());
    Hotel actualHotel = hotelCaptor.getValue();
    assertThat(actualHotel)
        .satisfies(
            h -> {
              assertThat(h.id().value()).isEqualTo(id);
              assertThat(h.name()).isEqualTo(name);
              assertThat(h.description()).isEqualTo(description);
              assertThat(h.location()).isEqualTo(location);
              assertThat(h.price()).isEqualTo(price);
              assertThat(h.imageURL()).isEqualTo(imageURL);
            });

    verify(eventBus).publish(domainEventsCaptor.capture());
    Set<DomainEvent> actualDomainEvents = domainEventsCaptor.getValue();
    assertThat(actualDomainEvents)
        .hasSize(1)
        .first()
        .isInstanceOf(HotelCreated.class)
        .satisfies(
            e -> {
              assertThat(e.id()).isEqualTo(domainEventId);
              assertThat(e.aggregateId().value()).isEqualTo(id);
              assertThat(e.occurredOn()).isEqualTo(NOW);
            });
  }

  @Test
  void notCreateHotelWhenAlreadyExists() {
    String id = UniqueId.get().value();
    String name = randomAlphabetic(5);
    String description = randomAlphabetic(10);
    String location = randomAlphabetic(5);
    Integer price = 100;
    String imageURL = "www." + randomAlphabetic(5) + ".com";

    Hotel existing = Hotel.load(id, name, description, location, price, imageURL);

    when(repository.find(new AggregateId(id))).thenReturn(Optional.of(existing));

    CreateHotelCommand command =
        new CreateHotelCommand(id, name, description, location, price, imageURL);
    testee.execute(command);

    verify(repository, never()).save(any(Hotel.class));
    verify(eventBus, never()).publish(anySet());
  }
}
