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
import org.egualpam.contexts.hotelmanagement.review.domain.Rating;
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

  @Captor private ArgumentCaptor<Hotel> hotelCaptor;
  @Captor private ArgumentCaptor<Set<DomainEvent>> domainEventsCaptor;

  @Mock private AggregateRepository<Hotel> repository;
  @Mock private EventBus eventBus;

  private UpdateHotelRating testee;

  @BeforeEach
  void setUp() {
    testee = new UpdateHotelRating(repository, eventBus);
  }

  @Test
  void updateHotelRating() {
    AggregateId aggregateId = new AggregateId(UniqueId.get().value());
    Rating rating = new Rating(3);
    Double expectedHotelRatingAverage = Double.parseDouble(rating.value().toString());
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
        new UpdateHotelRatingCommand(aggregateId.value(), rating.value());
    testee.execute(command);

    verify(repository).save(hotelCaptor.capture());
    Hotel saved = hotelCaptor.getValue();

    assertThat(saved.rating()).isEqualTo(expectedHotelRating);

    // TODO: Assert domain event has been published
    verify(eventBus).publish(domainEventsCaptor.capture());
    assertThat(domainEventsCaptor.getValue()).isEmpty();
  }

  @Test
  void throwDomainException_whenHotelMatchingIdNotExists() {
    AggregateId aggregateId = new AggregateId(UniqueId.get().value());
    Rating rating = new Rating(3);

    when(repository.find(aggregateId)).thenReturn(Optional.empty());

    UpdateHotelRatingCommand command =
        new UpdateHotelRatingCommand(aggregateId.value(), rating.value());

    HotelNotExists exception = assertThrows(HotelNotExists.class, () -> testee.execute(command));
    assertThat(exception)
        .hasMessage("No hotel exists with id: [%s]".formatted(aggregateId.value()));

    verify(repository, never()).save(any(Hotel.class));
    verify(eventBus, never()).publish(anySet());
  }
}
