package org.egualpam.contexts.hotelmanagement.hotel.application.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;
import org.egualpam.contexts.hotelmanagement.hotel.domain.Hotel;
import org.egualpam.contexts.hotelmanagement.hotel.domain.HotelNotExists;
import org.egualpam.contexts.hotelmanagement.hotel.domain.HotelRating;
import org.egualpam.contexts.hotelmanagement.hotel.domain.ReviewIsAlreadyProcessed;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateId;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateRepository;
import org.egualpam.contexts.hotelmanagement.shared.domain.DomainEvent;
import org.egualpam.contexts.hotelmanagement.shared.domain.EntityId;
import org.egualpam.contexts.hotelmanagement.shared.domain.EventBus;
import org.egualpam.contexts.hotelmanagement.shared.domain.InvalidUniqueId;
import org.egualpam.contexts.hotelmanagement.shared.domain.RequiredPropertyIsMissing;
import org.egualpam.contexts.hotelmanagement.shared.domain.UniqueId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

// TODO: Add test covering missing reviewId
@ExtendWith(MockitoExtension.class)
class UpdateHotelRatingShould {

  @Captor private ArgumentCaptor<Hotel> hotelCaptor;
  @Captor private ArgumentCaptor<Set<DomainEvent>> domainEventsCaptor;

  @Mock private AggregateRepository<Hotel> repository;
  @Mock private ReviewIsAlreadyProcessed reviewIsAlreadyProcessed;
  @Mock private EventBus eventBus;

  private UpdateHotelRating testee;

  @BeforeEach
  void setUp() {
    testee = new UpdateHotelRating(repository, reviewIsAlreadyProcessed, eventBus);
  }

  @Test
  void updateHotelRating() {
    AggregateId aggregateId = new AggregateId(UniqueId.get().value());
    EntityId reviewId = new EntityId(UniqueId.get().value());
    Integer rating = 3;

    Double expectedHotelRatingAverage = Double.parseDouble(rating.toString());
    HotelRating expectedHotelRating = new HotelRating(1, expectedHotelRatingAverage);

    Hotel hotel =
        new Hotel(
            aggregateId.value(),
            "hotel-name",
            "hotel-description",
            "hotel-location",
            200,
            "www.hotel-image-url.com");

    when(repository.find(aggregateId)).thenReturn(Optional.of(hotel));

    assertThat(hotel.rating().reviewsCount()).isZero();
    assertThat(hotel.rating().average()).isZero();

    UpdateHotelRatingCommand command =
        new UpdateHotelRatingCommand(aggregateId.value(), reviewId.value(), rating);
    testee.execute(command);

    verify(repository).save(hotelCaptor.capture());
    Hotel saved = hotelCaptor.getValue();

    assertThat(saved.rating()).isEqualTo(expectedHotelRating);

    // TODO: Assert domain event has been published
    verify(eventBus).publish(domainEventsCaptor.capture());
    assertThat(domainEventsCaptor.getValue()).isEmpty();
  }

  @Test
  void notUpdateHotelRating_whenReviewIsAlreadyProcessed() {
    AggregateId aggregateId = new AggregateId(UniqueId.get().value());
    EntityId reviewId = new EntityId(UniqueId.get().value());
    Integer rating = 3;

    Hotel hotel =
        new Hotel(
            aggregateId.value(),
            "hotel-name",
            "hotel-description",
            "hotel-location",
            200,
            "www.hotel-image-url.com");

    when(repository.find(aggregateId)).thenReturn(Optional.of(hotel));
    when(reviewIsAlreadyProcessed.with(reviewId)).thenReturn(true);

    UpdateHotelRatingCommand command =
        new UpdateHotelRatingCommand(aggregateId.value(), reviewId.value(), rating);
    testee.execute(command);

    verify(repository, never()).save(any(Hotel.class));
    verify(eventBus, never()).publish(anySet());
  }

  @Test
  void throwDomainException_whenHotelMatchingIdNotExists() {
    AggregateId aggregateId = new AggregateId(UniqueId.get().value());
    EntityId reviewId = new EntityId(UniqueId.get().value());
    Integer rating = 3;

    when(repository.find(aggregateId)).thenReturn(Optional.empty());

    UpdateHotelRatingCommand command =
        new UpdateHotelRatingCommand(aggregateId.value(), reviewId.value(), rating);

    HotelNotExists exception = assertThrows(HotelNotExists.class, () -> testee.execute(command));
    assertThat(exception)
        .hasMessage("No hotel exists with id: [%s]".formatted(aggregateId.value()));

    verify(repository, never()).save(any(Hotel.class));
    verify(eventBus, never()).publish(anySet());
  }

  @Test
  void throwDomainException_whenHotelIdIsMissing() {
    UpdateHotelRatingCommand command =
        new UpdateHotelRatingCommand(null, UniqueId.get().value(), 3);
    assertThrows(InvalidUniqueId.class, () -> testee.execute(command));

    verify(repository, never()).save(any(Hotel.class));
    verify(eventBus, never()).publish(anySet());
  }

  @Test
  void throwDomainException_whenReviewIdIsMissing() {
    AggregateId aggregateId = new AggregateId(UniqueId.get().value());

    Hotel hotel =
        new Hotel(
            aggregateId.value(),
            "hotel-name",
            "hotel-description",
            "hotel-location",
            200,
            "www.hotel-image-url.com");

    when(repository.find(aggregateId)).thenReturn(Optional.of(hotel));

    UpdateHotelRatingCommand command = new UpdateHotelRatingCommand(aggregateId.value(), null, 3);
    assertThrows(RequiredPropertyIsMissing.class, () -> testee.execute(command));

    verify(repository, never()).save(any(Hotel.class));
    verify(eventBus, never()).publish(anySet());
  }

  @Test
  void throwDomainException_whenReviewRatingIsMissing() {
    AggregateId aggregateId = new AggregateId(UniqueId.get().value());

    Hotel hotel =
        new Hotel(
            aggregateId.value(),
            "hotel-name",
            "hotel-description",
            "hotel-location",
            200,
            "www.hotel-image-url.com");

    when(repository.find(aggregateId)).thenReturn(Optional.of(hotel));

    UpdateHotelRatingCommand command =
        new UpdateHotelRatingCommand(aggregateId.value(), UniqueId.get().value(), null);
    assertThrows(RequiredPropertyIsMissing.class, () -> testee.execute(command));

    verify(repository, never()).save(any(Hotel.class));
    verify(eventBus, never()).publish(anySet());
  }
}
