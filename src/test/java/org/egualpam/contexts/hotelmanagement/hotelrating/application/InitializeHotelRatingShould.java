package org.egualpam.contexts.hotelmanagement.hotelrating.application;

import static java.util.Map.entry;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.egualpam.contexts.hotelmanagement.hotelrating.domain.HotelRating;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateId;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateRepository;
import org.egualpam.contexts.hotelmanagement.shared.domain.DomainEvent;
import org.egualpam.contexts.hotelmanagement.shared.domain.EventBus;
import org.egualpam.contexts.hotelmanagement.shared.domain.UniqueId;
import org.egualpam.contexts.hotelmanagement.shared.domain.UniqueIdSupplier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class InitializeHotelRatingShould {

  private static final Instant NOW = Instant.now();

  @Captor private ArgumentCaptor<HotelRating> hotelRatingCaptor;
  @Captor private ArgumentCaptor<Set<DomainEvent>> domainEventsCaptor;

  @Mock private UniqueIdSupplier uniqueIdSupplier;
  @Mock private Clock clock;
  @Mock private AggregateRepository<HotelRating> repository;
  @Mock private EventBus eventBus;

  private InitializeHotelRating testee;

  @BeforeEach
  void setUp() {
    testee = new InitializeHotelRating(uniqueIdSupplier, clock, repository, eventBus);
  }

  @Test
  void initializeHotelRating() {
    UniqueId id = UniqueId.get();
    UniqueId hotelId = UniqueId.get();
    UniqueId domainEventId = UniqueId.get();

    Map<String, Object> properties =
        Map.ofEntries(
            entry("id", id.value()),
            entry("hotelId", hotelId.value()),
            entry("reviewsCount", 0),
            entry("average", 0.0));
    HotelRating expected = HotelRating.load(properties);

    when(uniqueIdSupplier.get()).thenReturn(domainEventId);
    when(clock.instant()).thenReturn(NOW);

    InitializeHotelRatingCommand command =
        new InitializeHotelRatingCommand(id.value(), hotelId.value());

    testee.execute(command);

    verify(repository).save(hotelRatingCaptor.capture());

    HotelRating saved = hotelRatingCaptor.getValue();
    assertThat(saved).usingRecursiveComparison().isEqualTo(expected);

    verify(eventBus).publish(domainEventsCaptor.capture());
    Set<DomainEvent> published = domainEventsCaptor.getValue();
    assertThat(published)
        .hasSize(1)
        .first()
        .satisfies(
            event -> {
              assertThat(event.id()).isEqualTo(domainEventId);
              assertThat(event.occurredOn()).isEqualTo(NOW);
              assertThat(event.aggregateId()).isEqualTo(new AggregateId(id.value()));
            });
  }

  @Test
  void notInitializeHotelRating_whenAlreadyExists() {
    UniqueId id = UniqueId.get();
    UniqueId hotelId = UniqueId.get();
    InitializeHotelRatingCommand command =
        new InitializeHotelRatingCommand(id.value(), hotelId.value());

    Map<String, Object> properties =
        Map.ofEntries(
            entry("id", id.value()),
            entry("hotelId", hotelId.value()),
            entry("reviewsCount", 0),
            entry("average", 0.0));
    HotelRating existing = HotelRating.load(properties);

    when(repository.find(new AggregateId(id.value()))).thenReturn(Optional.of(existing));

    testee.execute(command);

    verify(repository, never()).save(hotelRatingCaptor.capture());
    verify(eventBus, never()).publish(domainEventsCaptor.capture());
  }
}
