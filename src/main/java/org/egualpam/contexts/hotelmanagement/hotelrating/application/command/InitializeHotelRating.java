package org.egualpam.contexts.hotelmanagement.hotelrating.application.command;

import java.time.Clock;
import org.egualpam.contexts.hotelmanagement.hotelrating.domain.HotelRating;
import org.egualpam.contexts.hotelmanagement.hotelrating.domain.HotelRatingAlreadyExists;
import org.egualpam.contexts.hotelmanagement.hotelrating.domain.HotelRatingIdGenerator;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateRepository;
import org.egualpam.contexts.hotelmanagement.shared.domain.EventBus;
import org.egualpam.contexts.hotelmanagement.shared.domain.UniqueIdSupplier;

public class InitializeHotelRating {

  private final UniqueIdSupplier uniqueIdSupplier;
  private final Clock clock;
  private final AggregateRepository<HotelRating> repository;
  private final HotelRatingIdGenerator hotelRatingIdGenerator;
  private final EventBus eventBus;

  public InitializeHotelRating(
      UniqueIdSupplier uniqueIdSupplier,
      Clock clock,
      HotelRatingIdGenerator hotelRatingIdGenerator,
      AggregateRepository<HotelRating> repository,
      EventBus eventBus) {
    this.uniqueIdSupplier = uniqueIdSupplier;
    this.clock = clock;
    this.hotelRatingIdGenerator = hotelRatingIdGenerator;
    this.repository = repository;
    this.eventBus = eventBus;
  }

  public void execute(InitializeHotelRatingCommand command) {
    String hotelId = command.hotelId();

    final HotelRating hotelRating;

    try {
      hotelRating =
          HotelRating.initialize(
              hotelRatingIdGenerator, repository, uniqueIdSupplier, clock, hotelId);
    } catch (HotelRatingAlreadyExists ignored) {
      return;
    }

    repository.save(hotelRating);
    eventBus.publish(hotelRating.pullDomainEvents());
  }
}
