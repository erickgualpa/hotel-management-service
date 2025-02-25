package org.egualpam.contexts.hotelmanagement.hotelrating.application.command;

import java.time.Clock;
import java.util.function.Supplier;
import org.egualpam.contexts.hotelmanagement.hotelrating.domain.HotelRating;
import org.egualpam.contexts.hotelmanagement.hotelrating.domain.HotelRatingNotFound;
import org.egualpam.contexts.hotelmanagement.hotelrating.domain.ReviewAlreadyProcessed;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateId;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateRepository;
import org.egualpam.contexts.hotelmanagement.shared.domain.EventBus;
import org.egualpam.contexts.hotelmanagement.shared.domain.UniqueId;

public class UpdateHotelRating {

  private final Supplier<UniqueId> uniqueIdSupplier;
  private final Clock clock;
  private final AggregateRepository<HotelRating> repository;
  private final EventBus eventBus;

  public UpdateHotelRating(
      Supplier<UniqueId> uniqueIdSupplier,
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
    String reviewId = command.reviewId();
    Integer reviewRating = command.reviewRating();

    AggregateId hotelRatingId = new AggregateId(id);

    HotelRating hotelRating =
        repository.find(hotelRatingId).orElseThrow(() -> new HotelRatingNotFound(hotelRatingId));

    try {
      hotelRating.update(uniqueIdSupplier, clock, reviewId, reviewRating);
    } catch (ReviewAlreadyProcessed ignored) {
      return;
    }

    repository.save(hotelRating);
    eventBus.publish(hotelRating.pullDomainEvents());
  }
}
