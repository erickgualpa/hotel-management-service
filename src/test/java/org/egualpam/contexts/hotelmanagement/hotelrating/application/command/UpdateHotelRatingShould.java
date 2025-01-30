package org.egualpam.contexts.hotelmanagement.hotelrating.application.command;

import static java.util.Map.entry;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.egualpam.contexts.hotelmanagement.hotelrating.domain.HotelRating;
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

  @Captor private ArgumentCaptor<Set<DomainEvent>> domainEventsCaptor;
  @Captor private ArgumentCaptor<HotelRating> hotelRatingCaptor;
  @Mock private AggregateRepository<HotelRating> repository;
  @Mock private EventBus eventBus;

  private UpdateHotelRating testee;

  @BeforeEach
  void setUp() {
    testee = new UpdateHotelRating(repository, eventBus);
  }

  @Test
  void updateHotelRating() {
    String id = UniqueId.get().value();
    String hotelId = UniqueId.get().value();

    Map<String, Object> properties =
        Map.ofEntries(
            entry("id", id),
            entry("hotelId", hotelId),
            entry("reviewsCount", 0),
            entry("average", 0.0));
    HotelRating existing = HotelRating.load(properties);

    when(repository.find(new AggregateId(id))).thenReturn(Optional.of(existing));

    UpdateHotelRatingCommand command = new UpdateHotelRatingCommand(id);
    testee.execute(command);

    verify(repository).save(existing);
    verify(eventBus).publish(domainEventsCaptor.capture());

    Set<DomainEvent> published = domainEventsCaptor.getValue();
    assertThat(published).isEmpty();
  }
}
