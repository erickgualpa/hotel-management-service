package org.egualpam.contexts.hotelmanagement.hotelrating.application.command;

import org.egualpam.contexts.hotelmanagement.hotelrating.domain.HotelRating;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateRepository;
import org.egualpam.contexts.hotelmanagement.shared.domain.EventBus;

public class UpdateHotelRating {

  private final AggregateRepository<HotelRating> repository;
  private final EventBus eventBus;

  public UpdateHotelRating(AggregateRepository<HotelRating> repository, EventBus eventBus) {
    this.repository = repository;
    this.eventBus = eventBus;
  }

  public void execute(UpdateHotelRatingCommand command) {}
}
