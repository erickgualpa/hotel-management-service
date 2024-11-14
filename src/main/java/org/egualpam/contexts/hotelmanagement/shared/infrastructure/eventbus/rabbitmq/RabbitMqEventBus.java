package org.egualpam.contexts.hotelmanagement.shared.infrastructure.eventbus.rabbitmq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeoutException;
import org.egualpam.contexts.hotelmanagement.shared.domain.DomainEvent;
import org.egualpam.contexts.hotelmanagement.shared.domain.EventBus;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.eventbus.events.PublicEvent;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.eventbus.events.PublicEventFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO: Refactor this whole class
public final class RabbitMqEventBus implements EventBus {

  private final Logger logger = LoggerFactory.getLogger(RabbitMqEventBus.class);

  private final Connection connection;
  private final ObjectMapper objectMapper;

  public RabbitMqEventBus(Connection connection, ObjectMapper objectMapper) {
    this.connection = connection;
    this.objectMapper = objectMapper;
  }

  @Override
  public void publish(Set<DomainEvent> events) {
    List<PublicEvent> publicEvents = events.stream().map(PublicEventFactory::from).toList();
    try (Channel channel = connection.createChannel()) {
      // TODO: Queues should be already configured
      channel.queueDeclare("hotelmanagement.hotels", false, false, false, null);
      channel.queueDeclare("hotelmanagement.reviews", false, false, false, null);
      // TODO: Workaround for having a dlq but should be updated too
      channel.queueDeclare("hotelmanagement.dlq", false, false, false, null);

      publicEvents.forEach(e -> publishEvent(e, channel));
    } catch (IOException | TimeoutException e) {
      // TODO: Consider using a custom exception
      throw new RuntimeException(e);
    }
  }

  private void publishEvent(PublicEvent publicEvent, Channel channel) {
    try {
      byte[] serializedEvent = objectMapper.writeValueAsBytes(publicEvent);
      channel.basicPublish("", getQueueNameFor(publicEvent), null, serializedEvent);
      logger.info("Event {} has been published", publicEvent.getType());
    } catch (JsonProcessingException ex) {
      // TODO: Consider using a custom exception
      throw new RuntimeException("Domain event could not be serialized", ex);
    } catch (IOException ex) {
      throw new RuntimeException("Domain event could not be published", ex);
    }
  }

  // TODO: Simple mapping for events and queues. Update it
  private String getQueueNameFor(PublicEvent publicEvent) {
    return switch (publicEvent.getType()) {
      case "hotelmanagement.hotels.created.v1.0" -> "hotelmanagement.hotels";
      case "hotelmanagement.reviews.created.v1.0", "hotelmanagement.reviews.updated.v1.0" ->
          "hotelmanagement.reviews";
      default -> "hotelmanagement.dlq";
    };
  }
}
