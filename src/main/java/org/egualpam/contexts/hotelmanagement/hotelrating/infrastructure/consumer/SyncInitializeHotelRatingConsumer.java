package org.egualpam.contexts.hotelmanagement.hotelrating.infrastructure.consumer;

import static org.slf4j.LoggerFactory.getLogger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import java.io.IOException;
import org.egualpam.contexts.hotelmanagement.hotelrating.infrastructure.cqrs.command.simple.SyncInitializeHotelRatingCommand;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.command.CommandBus;
import org.slf4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

public class SyncInitializeHotelRatingConsumer {

  private final Logger logger = getLogger(this.getClass());

  private final ObjectMapper objectMapper;
  private final CommandBus commandBus;

  public SyncInitializeHotelRatingConsumer(ObjectMapper objectMapper, CommandBus commandBus) {
    this.objectMapper = objectMapper;
    this.commandBus = commandBus;
  }

  @RabbitListener(
      id = "sync-initialize-hotel-rating-consumer",
      queues = "hotelmanagement.hotel-rating.initialize-hotel-rating",
      ackMode = "MANUAL")
  public void consume(Message in, Channel channel) throws IOException {
    var event = objectMapper.readValue(in.getBody(), JsonNode.class);
    var eventType = event.get("type").asText();
    var eventVersion = event.get("version").asText();

    if (!"hotelmanagement.hotel.created".equals(eventType) || !"1.0".equals(eventVersion)) {
      return;
    }

    var hotelCreatedEvent = objectMapper.readValue(in.getBody(), HotelCreatedEvent.class);

    var hotelId = hotelCreatedEvent.aggregateId();
    var syncInitializeHotelRatingCommand = new SyncInitializeHotelRatingCommand(hotelId);

    try {
      commandBus.publish(syncInitializeHotelRatingCommand);
    } catch (RuntimeException e) {
      logger.error("An error occurred while processing the event [{}]", event, e);
      // TODO: Check how to handle ACK when an exception occurs
    }

    channel.basicAck(in.getMessageProperties().getDeliveryTag(), true);
  }

  private record HotelCreatedEvent(String aggregateId) {}
}
