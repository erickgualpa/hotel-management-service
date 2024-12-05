package org.egualpam.contexts.hotelmanagement.shared.infrastructure.eventbus.rabbitmq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import org.egualpam.contexts.hotelmanagement.shared.domain.DomainEvent;
import org.egualpam.contexts.hotelmanagement.shared.domain.EventBus;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.eventbus.events.PublicEvent;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.eventbus.events.PublicEventFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO: Refactor this whole class
public final class RabbitMqEventBus implements EventBus {

  private final Logger logger = LoggerFactory.getLogger(RabbitMqEventBus.class);

  private final Channel channel;
  private final ObjectMapper objectMapper;

  public RabbitMqEventBus(Channel channel, ObjectMapper objectMapper) {
    this.channel = channel;
    this.objectMapper = objectMapper;
  }

  @Override
  public void publish(Set<DomainEvent> events) {
    List<PublicEvent> publicEvents = events.stream().map(PublicEventFactory::from).toList();
    publicEvents.forEach(e -> publishEvent(e, channel));
  }

  private void publishEvent(PublicEvent publicEvent, Channel channel) {
    try {
      byte[] serializedEvent = objectMapper.writeValueAsBytes(publicEvent);

      String queueName = getQueueNameFor(publicEvent);
      channel.basicPublish("", queueName, null, serializedEvent);

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
