package org.egualpam.contexts.hotelmanagement.hotel.application.command;

import java.util.Optional;
import org.egualpam.contexts.hotelmanagement.hotel.domain.Hotel;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateId;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateRepository;
import org.egualpam.contexts.hotelmanagement.shared.domain.EventBus;

public class UpdateHotelRating {

  private final AggregateRepository<Hotel> repository;
  private final EventBus eventBus;

  public UpdateHotelRating(AggregateRepository<Hotel> repository, EventBus eventBus) {
    this.repository = repository;
    this.eventBus = eventBus;
  }

  public void execute(UpdateHotelRatingCommand command) {
    String hotelId = command.hotelId();
    Integer reviewRating = command.rating();

    Hotel hotel =
        Optional.of(hotelId)
            .map(AggregateId::new)
            .flatMap(repository::find)
            .orElseThrow(); // TODO: Replace by custom exception

    // TODO: Handle idempotency
    hotel.updateRating(reviewRating);

    repository.save(hotel);
    eventBus.publish(hotel.pullDomainEvents());
  }
}
