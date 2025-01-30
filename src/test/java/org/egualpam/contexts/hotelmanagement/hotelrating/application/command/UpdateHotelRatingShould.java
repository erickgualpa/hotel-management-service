package org.egualpam.contexts.hotelmanagement.hotelrating.application.command;

import org.egualpam.contexts.hotelmanagement.hotelrating.domain.HotelRating;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateRepository;
import org.egualpam.contexts.hotelmanagement.shared.domain.EventBus;
import org.egualpam.contexts.hotelmanagement.shared.domain.UniqueId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UpdateHotelRatingShould {

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
    UpdateHotelRatingCommand command = new UpdateHotelRatingCommand(id);

    testee.execute(command);
  }
}
