package org.egualpam.contexts.hotelmanagement.hotel.application.command;

import org.egualpam.contexts.hotelmanagement.hotel.domain.Hotel;
import org.egualpam.contexts.hotelmanagement.hotel.domain.HotelNotExists;
import org.egualpam.contexts.hotelmanagement.hotel.domain.ReviewAlreadyProcessed;
import org.egualpam.contexts.hotelmanagement.hotel.domain.ReviewIsAlreadyProcessed;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateId;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateRepository;
import org.egualpam.contexts.hotelmanagement.shared.domain.EventBus;

public class UpdateHotelRating {

  private final AggregateRepository<Hotel> repository;
  private final ReviewIsAlreadyProcessed reviewIsAlreadyProcessed;
  private final EventBus eventBus;

  public UpdateHotelRating(
      AggregateRepository<Hotel> repository,
      ReviewIsAlreadyProcessed reviewIsAlreadyProcessed,
      EventBus eventBus) {
    this.repository = repository;
    this.reviewIsAlreadyProcessed = reviewIsAlreadyProcessed;
    this.eventBus = eventBus;
  }

  public void execute(UpdateHotelRatingCommand command) {
    final String hotelId = command.hotelId();
    final String reviewId = command.reviewId();
    final Integer reviewRating = command.reviewRating();

    final AggregateId aggregateId = new AggregateId(hotelId);
    final Hotel hotel =
        repository.find(aggregateId).orElseThrow(() -> new HotelNotExists(aggregateId));

    try {
      hotel.updateRating(reviewId, reviewRating, reviewIsAlreadyProcessed);
    } catch (ReviewAlreadyProcessed e) {
      return;
    }

    repository.save(hotel);
    eventBus.publish(hotel.pullDomainEvents());
  }
}
