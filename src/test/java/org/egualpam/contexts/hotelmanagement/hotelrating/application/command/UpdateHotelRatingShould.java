package org.egualpam.contexts.hotelmanagement.hotelrating.application.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import org.egualpam.contexts.hotelmanagement.hotelrating.domain.HotelRating;
import org.egualpam.contexts.hotelmanagement.hotelrating.domain.HotelRatingIdGenerator;
import org.egualpam.contexts.hotelmanagement.hotelrating.domain.HotelRatingNotFound;
import org.egualpam.contexts.hotelmanagement.hotelrating.domain.HotelRatingUpdated;
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
class UpdateHotelRatingShould {

  private static final Instant NOW = Instant.now();

  @Captor private ArgumentCaptor<Set<DomainEvent>> domainEventsCaptor;
  @Captor private ArgumentCaptor<HotelRating> hotelRatingCaptor;

  @Mock private Supplier<UniqueId> uniqueIdSupplier;
  @Mock private Clock clock;
  @Mock private HotelRatingIdGenerator hotelRatingIdGenerator;
  @Mock private AggregateRepository<HotelRating> repository;
  @Mock private EventBus eventBus;

  private UpdateHotelRating testee;

  @BeforeEach
  void setUp() {
    testee =
        new UpdateHotelRating(
            uniqueIdSupplier, clock, hotelRatingIdGenerator, repository, eventBus);
  }

  @Test
  void updateHotelRating() {
    String id = UniqueId.get().value();
    String hotelId = UniqueId.get().value();
    String existingReviewId = UniqueId.get().value();
    String anotherExistingReview = UniqueId.get().value();
    String reviewId = UniqueId.get().value();
    Integer reviewRating = 4;
    UniqueId domainEventId = UniqueId.get();
    AggregateId aggregateId = new AggregateId(id);

    Set<String> existingReviews = Set.of(existingReviewId, anotherExistingReview);
    HotelRating existing = HotelRating.load(id, hotelId, existingReviews, 4.0);

    when(clock.instant()).thenReturn(NOW);
    when(uniqueIdSupplier.get()).thenReturn(domainEventId);
    when(hotelRatingIdGenerator.generate(new UniqueId(hotelId))).thenReturn(aggregateId);
    when(repository.find(aggregateId)).thenReturn(Optional.of(existing));

    UpdateHotelRatingCommand command =
        new UpdateHotelRatingCommand(hotelId, reviewId, reviewRating);
    testee.execute(command);

    verify(repository).save(hotelRatingCaptor.capture());
    HotelRating saved = hotelRatingCaptor.getValue();
    assertThat(saved)
        .satisfies(
            actual -> {
              assertThat(actual.reviews())
                  .containsExactlyInAnyOrder(existingReviewId, anotherExistingReview, reviewId);
              assertThat(actual.average()).isEqualTo(4.0);
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

  @Test
  void notUpdateHotelRating_whenReviewIsAlreadyProcessed() {
    String id = UniqueId.get().value();
    String hotelId = UniqueId.get().value();
    String reviewId = UniqueId.get().value();
    Integer reviewRating = 3;
    AggregateId aggregateId = new AggregateId(id);

    HotelRating existing = HotelRating.load(id, hotelId, Set.of(reviewId), 3.0);

    when(hotelRatingIdGenerator.generate(new UniqueId(hotelId))).thenReturn(aggregateId);
    when(repository.find(aggregateId)).thenReturn(Optional.of(existing));

    UpdateHotelRatingCommand command =
        new UpdateHotelRatingCommand(hotelId, reviewId, reviewRating);
    testee.execute(command);

    verify(repository, never()).save(any());
    verify(eventBus, never()).publish(any());
  }

  @Test
  void throwDomainException_whenHotelRatingIsNotFound() {
    AggregateId aggregateId = new AggregateId(UniqueId.get().value());
    String hotelId = UniqueId.get().value();
    String reviewId = UniqueId.get().value();
    Integer reviewRating = 2;

    when(hotelRatingIdGenerator.generate(new UniqueId(hotelId))).thenReturn(aggregateId);
    when(repository.find(aggregateId)).thenReturn(Optional.empty());

    UpdateHotelRatingCommand command =
        new UpdateHotelRatingCommand(hotelId, reviewId, reviewRating);
    assertThrows(HotelRatingNotFound.class, () -> testee.execute(command));
  }
}
