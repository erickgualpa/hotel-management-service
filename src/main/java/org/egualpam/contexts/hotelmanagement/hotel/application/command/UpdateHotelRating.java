package org.egualpam.contexts.hotelmanagement.hotel.application.command;

import org.egualpam.contexts.hotelmanagement.hotel.domain.Hotel;
import org.egualpam.contexts.hotelmanagement.hotel.domain.HotelNotExists;
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

    AggregateId aggregateId = new AggregateId(hotelId);
    Hotel hotel = repository.find(aggregateId).orElseThrow(() -> new HotelNotExists(aggregateId));

    // TODO: Handle idempotency
    hotel.updateRating(reviewRating);

    repository.save(hotel);
    eventBus.publish(hotel.pullDomainEvents());
  }
}
