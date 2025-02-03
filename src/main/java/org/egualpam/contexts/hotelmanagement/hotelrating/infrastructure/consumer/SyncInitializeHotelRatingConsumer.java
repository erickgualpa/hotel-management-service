package org.egualpam.contexts.hotelmanagement.hotelrating.infrastructure.consumer;

import static org.slf4j.LoggerFactory.getLogger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import java.io.IOException;
import org.egualpam.contexts.hotelmanagement.hotelrating.infrastructure.cqrs.command.simple.SyncInitializeHotelRatingCommand;
import org.egualpam.contexts.hotelmanagement.shared.domain.UniqueIdSupplier;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.command.Command;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.command.CommandBus;
import org.slf4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

public class SyncInitializeHotelRatingConsumer {

  private final Logger logger = getLogger(this.getClass());

  private final ObjectMapper objectMapper;
  private final UniqueIdSupplier uniqueIdSupplier;
  private final CommandBus commandBus;

  public SyncInitializeHotelRatingConsumer(
      ObjectMapper objectMapper, UniqueIdSupplier uniqueIdSupplier, CommandBus commandBus) {
    this.objectMapper = objectMapper;
    this.uniqueIdSupplier = uniqueIdSupplier;
    this.commandBus = commandBus;
  }

  @RabbitListener(
      id = "sync-initialize-hotel-rating-consumer",
      queues = "hotelmanagement.hotel-rating.initialize-hotel-rating",
      ackMode = "MANUAL")
  public void consume(Message in, Channel channel) throws IOException {
    JsonNode event = objectMapper.readValue(in.getBody(), JsonNode.class);
    String eventType = event.get("type").asText();

    if (!"hotelmanagement.hotel.created".equals(eventType)) {
      return;
    }

    HotelCreatedEvent hotelCreatedEvent =
        objectMapper.readValue(in.getBody(), HotelCreatedEvent.class);

    String id = uniqueIdSupplier.get().value();
    String hotelId = hotelCreatedEvent.aggregateId();
    Command syncInitializeHotelRatingCommand = new SyncInitializeHotelRatingCommand(id, hotelId);

    try {
      commandBus.publish(syncInitializeHotelRatingCommand);
    } catch (RuntimeException e) {
      logger.error("An error occurred while processing the event [%s]".formatted(event), e);
      // TODO: Check how to handle ACK when an exception occurs
    }

    channel.basicAck(in.getMessageProperties().getDeliveryTag(), true);
  }

  private record HotelCreatedEvent(String aggregateId) {}
}
