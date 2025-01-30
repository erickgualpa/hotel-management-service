package org.egualpam.contexts.hotelmanagement.hotelrating.application.command;

import java.time.Clock;
import org.egualpam.contexts.hotelmanagement.hotelrating.domain.HotelRating;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateId;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateRepository;
import org.egualpam.contexts.hotelmanagement.shared.domain.EventBus;
import org.egualpam.contexts.hotelmanagement.shared.domain.UniqueIdSupplier;

public class UpdateHotelRating {

  private final UniqueIdSupplier uniqueIdSupplier;
  private final Clock clock;
  private final AggregateRepository<HotelRating> repository;
  private final EventBus eventBus;

  public UpdateHotelRating(
      UniqueIdSupplier uniqueIdSupplier,
      Clock clock,
      AggregateRepository<HotelRating> repository,
      EventBus eventBus) {
    this.uniqueIdSupplier = uniqueIdSupplier;
    this.clock = clock;
    this.repository = repository;
    this.eventBus = eventBus;
  }

  public void execute(UpdateHotelRatingCommand command) {
    String id = command.id();
    Integer reviewRating = command.reviewRating();

    HotelRating hotelRating =
        repository
            .find(new AggregateId(id))
            // TODO: Replace by custom exception
            .orElseThrow();

    hotelRating.update(uniqueIdSupplier, clock, reviewRating);

    repository.save(hotelRating);
    eventBus.publish(hotelRating.pullDomainEvents());
  }
}
