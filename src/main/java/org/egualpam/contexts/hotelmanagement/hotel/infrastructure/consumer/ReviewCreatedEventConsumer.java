package org.egualpam.contexts.hotelmanagement.hotel.infrastructure.consumer;

import static org.slf4j.LoggerFactory.getLogger;

import org.egualpam.contexts.hotelmanagement.hotel.infrastructure.cqrs.command.simple.SyncUpdateHotelRatingCommand;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.command.Command;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.command.CommandBus;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.internaleventbus.spring.ReviewCreatedEvent;
import org.slf4j.Logger;
import org.springframework.context.ApplicationListener;

public class ReviewCreatedEventConsumer implements ApplicationListener<ReviewCreatedEvent> {

  private final CommandBus commandBus;
  private final Logger logger = getLogger(this.getClass());

  public ReviewCreatedEventConsumer(CommandBus commandBus) {
    this.commandBus = commandBus;
  }

  @Override
  public void onApplicationEvent(ReviewCreatedEvent event) {
    String hotelId = event.hotelId();
    String reviewId = event.reviewId();
    Integer rating = event.rating();
    Command command = new SyncUpdateHotelRatingCommand(hotelId, reviewId, rating);
    try {
      commandBus.publish(command);
    } catch (RuntimeException e) {
      logger.error("An error occurred while processing the event [%s]".formatted(event), e);
    }
  }
}
