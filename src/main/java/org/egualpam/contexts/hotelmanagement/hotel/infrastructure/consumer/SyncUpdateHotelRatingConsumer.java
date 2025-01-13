package org.egualpam.contexts.hotelmanagement.hotel.infrastructure.consumer;

import static org.slf4j.LoggerFactory.getLogger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import java.io.IOException;
import org.egualpam.contexts.hotelmanagement.hotel.infrastructure.cqrs.command.simple.SyncUpdateHotelRatingCommand;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.command.Command;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.command.CommandBus;
import org.slf4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

public class SyncUpdateHotelRatingConsumer {

  private final ObjectMapper objectMapper;
  private final Logger logger = getLogger(this.getClass());
  private final CommandBus commandBus;

  public SyncUpdateHotelRatingConsumer(ObjectMapper objectMapper, CommandBus commandBus) {
    this.objectMapper = objectMapper;
    this.commandBus = commandBus;
  }

  @RabbitListener(
      id = "sync-update-hotel-rating-consumer",
      queues = "hotelmanagement.hotel.update-hotel-rating",
      ackMode = "MANUAL")
  public void consume(Message in, Channel channel) throws IOException {
    JsonNode event = objectMapper.readValue(in.getBody(), JsonNode.class);
    String eventType = event.get("type").asText();

    if (!"hotelmanagement.review.created".equals(eventType)) {
      return;
    }

    ReviewCreatedEvent reviewCreatedEvent =
        objectMapper.readValue(in.getBody(), ReviewCreatedEvent.class);

    String hotelId = reviewCreatedEvent.hotelId();
    String reviewId = reviewCreatedEvent.aggregateId();
    Integer rating = reviewCreatedEvent.reviewRating();

    Command command = new SyncUpdateHotelRatingCommand(hotelId, reviewId, rating);
    try {
      commandBus.publish(command);
    } catch (RuntimeException e) {
      logger.error("An error occurred while processing the event [%s]".formatted(event), e);
      // TODO: Check how to handle ACK when an exception occurs
    }

    channel.basicAck(in.getMessageProperties().getDeliveryTag(), true);
  }

  private record ReviewCreatedEvent(String aggregateId, String hotelId, Integer reviewRating) {}
}
