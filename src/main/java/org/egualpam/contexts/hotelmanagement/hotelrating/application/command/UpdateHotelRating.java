package org.egualpam.contexts.hotelmanagement.hotelrating.application.command;

import java.time.Clock;
import java.util.function.Supplier;
import org.egualpam.contexts.hotelmanagement.hotelrating.domain.HotelRating;
import org.egualpam.contexts.hotelmanagement.hotelrating.domain.HotelRatingIdGenerator;
import org.egualpam.contexts.hotelmanagement.hotelrating.domain.HotelRatingNotFound;
import org.egualpam.contexts.hotelmanagement.hotelrating.domain.ReviewAlreadyProcessed;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateId;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateRepository;
import org.egualpam.contexts.hotelmanagement.shared.domain.EventBus;
import org.egualpam.contexts.hotelmanagement.shared.domain.UniqueId;

public class UpdateHotelRating {

  private final Supplier<UniqueId> uniqueIdSupplier;
  private final Clock clock;
  private final HotelRatingIdGenerator hotelRatingIdGenerator;
  private final AggregateRepository<HotelRating> repository;
  private final EventBus eventBus;

  public UpdateHotelRating(
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

  public void execute(UpdateHotelRatingCommand command) {
    var hotelId = command.hotelId();
    var reviewId = command.reviewId();
    var reviewRating = command.reviewRating();

    var hotelRatingId = hotelRatingIdGenerator.generate(new AggregateId(hotelId));

    var hotelRating =
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
