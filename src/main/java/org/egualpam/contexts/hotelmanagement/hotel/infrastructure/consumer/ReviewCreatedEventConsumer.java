package org.egualpam.contexts.hotelmanagement.hotel.infrastructure.consumer;

import org.egualpam.contexts.hotelmanagement.hotel.application.command.UpdateHotelRating;
import org.egualpam.contexts.hotelmanagement.hotel.application.command.UpdateHotelRatingCommand;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.internaleventbus.spring.ReviewCreatedEvent;
import org.springframework.context.ApplicationListener;

public class ReviewCreatedEventConsumer implements ApplicationListener<ReviewCreatedEvent> {

  private final UpdateHotelRating updateHotelRating;

  public ReviewCreatedEventConsumer(UpdateHotelRating updateHotelRating) {
    this.updateHotelRating = updateHotelRating;
  }

  @Override
  public void onApplicationEvent(ReviewCreatedEvent event) {
    String hotelId = event.hotelId();
    String reviewId = event.reviewId();
    Integer rating = event.rating();
    UpdateHotelRatingCommand command = new UpdateHotelRatingCommand(hotelId, reviewId, rating);
    updateHotelRating.execute(command);
  }
}
