package org.egualpam.contexts.hotelmanagement.hotelrating.application.command;

import static java.util.Map.entry;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.egualpam.contexts.hotelmanagement.hotelrating.domain.HotelRating;
import org.egualpam.contexts.hotelmanagement.hotelrating.domain.HotelRatingUpdated;
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
class UpdateHotelRatingShould {

  private static final Instant NOW = Instant.now();

  @Captor private ArgumentCaptor<Set<DomainEvent>> domainEventsCaptor;
  @Captor private ArgumentCaptor<HotelRating> hotelRatingCaptor;

  @Mock private UniqueIdSupplier uniqueIdSupplier;
  @Mock private Clock clock;
  @Mock private AggregateRepository<HotelRating> repository;
  @Mock private EventBus eventBus;

  private UpdateHotelRating testee;

  @BeforeEach
  void setUp() {
    testee = new UpdateHotelRating(uniqueIdSupplier, clock, repository, eventBus);
  }

  @Test
  void updateHotelRating() {
    String id = UniqueId.get().value();
    String hotelId = UniqueId.get().value();
    Integer reviewRating = 2;
    UniqueId domainEventId = UniqueId.get();

    Map<String, Object> properties =
        Map.ofEntries(
            entry("id", id),
            entry("hotelId", hotelId),
            entry("reviewsCount", 1),
            entry("average", 3.0));
    HotelRating existing = HotelRating.load(properties);

    when(clock.instant()).thenReturn(NOW);
    when(uniqueIdSupplier.get()).thenReturn(domainEventId);
    when(repository.find(new AggregateId(id))).thenReturn(Optional.of(existing));

    UpdateHotelRatingCommand command = new UpdateHotelRatingCommand(id, reviewRating);
    testee.execute(command);

    verify(repository).save(hotelRatingCaptor.capture());
    HotelRating saved = hotelRatingCaptor.getValue();
    assertThat(saved)
        .satisfies(
            actual -> {
              assertThat(actual.reviewsCount()).isEqualTo(2);
              assertThat(actual.average()).isEqualTo(2.5);
            });

    verify(eventBus).publish(domainEventsCaptor.capture());

    Set<DomainEvent> published = domainEventsCaptor.getValue();
    assertThat(published)
        .hasSize(1)
        .first()
        .satisfies(
            actual -> {
              assertThat(actual).isInstanceOf(HotelRatingUpdated.class);
              assertThat(actual.id()).isEqualTo(domainEventId);
              assertThat(actual.aggregateId().value()).isEqualTo(id);
              assertThat(actual.occurredOn()).isEqualTo(NOW);
            });
  }
}
