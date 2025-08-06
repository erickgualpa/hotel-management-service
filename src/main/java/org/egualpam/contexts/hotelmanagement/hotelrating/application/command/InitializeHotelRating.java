package org.egualpam.contexts.hotelmanagement.hotelrating.application.command;

import java.time.Clock;
import java.util.function.Supplier;
import org.egualpam.contexts.hotel.shared.domain.AggregateRepository;
import org.egualpam.contexts.hotel.shared.domain.EventBus;
import org.egualpam.contexts.hotel.shared.domain.UniqueId;
import org.egualpam.contexts.hotelmanagement.hotelrating.domain.HotelRating;
import org.egualpam.contexts.hotelmanagement.hotelrating.domain.HotelRatingAlreadyExists;
import org.egualpam.contexts.hotelmanagement.hotelrating.domain.HotelRatingIdGenerator;

public class InitializeHotelRating {

  private final Supplier<UniqueId> uniqueIdSupplier;
  private final Clock clock;
  private final AggregateRepository<HotelRating> repository;
  private final HotelRatingIdGenerator hotelRatingIdGenerator;
  private final EventBus eventBus;

  public InitializeHotelRating(
      Supplier<UniqueId> uniqueIdSupplier,
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
    var hotelId = command.hotelId();

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
