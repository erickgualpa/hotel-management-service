package org.egualpam.contexts.hotelmanagement.hotel.application.command;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

import java.util.List;
import org.egualpam.contexts.hotelmanagement.hotel.domain.Hotel;
import org.egualpam.contexts.hotelmanagement.hotel.domain.HotelCreatedEvent;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateRepository;
import org.egualpam.contexts.hotelmanagement.shared.domain.DomainEvent;
import org.egualpam.contexts.hotelmanagement.shared.domain.EventBus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateHotelShould {

  @Mock private AggregateRepository<Hotel> repository;
  @Mock private EventBus eventBus;

  @Captor private ArgumentCaptor<Hotel> hotelCaptor;
  @Captor private ArgumentCaptor<List<DomainEvent>> domainEventsCaptor;

  private CreateHotel testee;

  @BeforeEach
  void setUp() {
    testee = new CreateHotel(repository, eventBus);
  }

  @Test
  void createHotel() {
    String id = randomUUID().toString();
    String name = randomAlphabetic(5);
    String description = randomAlphabetic(10);
    String location = randomAlphabetic(5);
    Integer price = 100;
    String imageURL = "www." + randomAlphabetic(5) + ".com";

    CreateHotelCommand command =
        new CreateHotelCommand(id, name, description, location, price, imageURL);
    testee.execute(command);

    verify(repository).save(hotelCaptor.capture());
    Hotel actualHotel = hotelCaptor.getValue();
    assertThat(actualHotel)
        .satisfies(
            h -> {
              assertThat(h.id().value()).isEqualTo(id);
              assertThat(h.name().value()).isEqualTo(name);
              assertThat(h.description().value()).isEqualTo(description);
              assertThat(h.location().value()).isEqualTo(location);
              assertThat(h.price().value()).isEqualTo(price);
              assertThat(h.imageURL().value()).isEqualTo(imageURL);
            });

    verify(eventBus).publish(domainEventsCaptor.capture());
    List<DomainEvent> actualDomainEvents = domainEventsCaptor.getValue();
    assertThat(actualDomainEvents)
        .hasSize(1)
        .first()
        .isInstanceOf(HotelCreatedEvent.class)
        .satisfies(
            e -> {
              assertNotNull(e.id());
              assertThat(e.aggregateId().value()).isEqualTo(id);
              assertNotNull(e.occurredOn());
            });
  }
}
