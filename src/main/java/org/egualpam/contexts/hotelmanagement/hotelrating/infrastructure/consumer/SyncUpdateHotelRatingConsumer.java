package org.egualpam.contexts.hotelmanagement.hotelrating.infrastructure.consumer;

import static org.slf4j.LoggerFactory.getLogger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import java.io.IOException;
import org.egualpam.contexts.hotelmanagement.hotelrating.infrastructure.cqrs.command.simple.SyncUpdateHotelRatingCommand;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.command.Command;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.command.CommandBus;
import org.slf4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

public class SyncUpdateHotelRatingConsumer {

  private final Logger logger = getLogger(this.getClass());

  private final ObjectMapper objectMapper;
  private final CommandBus commandBus;

  public SyncUpdateHotelRatingConsumer(ObjectMapper objectMapper, CommandBus commandBus) {
    this.objectMapper = objectMapper;
    this.commandBus = commandBus;
  }

  @RabbitListener(
      id = "sync-update-hotel-rating-consumer-v2",
      queues = "hotelmanagement.hotel-rating.update-hotel-rating",
      ackMode = "MANUAL")
  public void consume(Message in, Channel channel) throws IOException {
    JsonNode event = objectMapper.readValue(in.getBody(), JsonNode.class);
    String eventType = event.get("type").asText();
    String eventVersion = event.get("version").asText();

    if (!"hotelmanagement.review.created".equals(eventType) || !"1.0".equals(eventVersion)) {
      return;
    }

    ReviewCreatedEvent reviewCreatedEvent =
        objectMapper.readValue(in.getBody(), ReviewCreatedEvent.class);

    String hotelId = reviewCreatedEvent.hotelId();
    String reviewId = reviewCreatedEvent.aggregateId();
    Integer reviewRating = reviewCreatedEvent.reviewRating();

    final Command syncUpdateHotelRatingCommand =
        new SyncUpdateHotelRatingCommand(hotelId, reviewId, reviewRating);
    try {
      commandBus.publish(syncUpdateHotelRatingCommand);
    } catch (RuntimeException e) {
      logger.error("An error occurred while processing the event [%s]".formatted(event), e);
      // TODO: Check how to handle ACK when an exception occurs
    }

    channel.basicAck(in.getMessageProperties().getDeliveryTag(), true);
  }

  record ReviewCreatedEvent(String aggregateId, String hotelId, Integer reviewRating) {}
}
