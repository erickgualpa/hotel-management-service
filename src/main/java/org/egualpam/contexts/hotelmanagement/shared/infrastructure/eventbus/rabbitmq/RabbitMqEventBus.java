package org.egualpam.contexts.hotelmanagement.shared.infrastructure.eventbus.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import java.io.IOException;
import java.util.Set;
import org.egualpam.contexts.hotelmanagement.shared.domain.DomainEvent;
import org.egualpam.contexts.hotelmanagement.shared.domain.EventBus;
import org.egualpam.contexts.hotelmanagement.shared.domain.UnpublishedDomainEvent;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.eventbus.events.PublicEvent;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.eventbus.events.PublicEventFactory;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.eventbus.shared.EventStoreRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class RabbitMqEventBus implements EventBus {

  private final Logger logger = LoggerFactory.getLogger(RabbitMqEventBus.class);

  private final Channel channel;
  private final ObjectMapper objectMapper;
  private final EventStoreRepository eventStoreRepository;

  public RabbitMqEventBus(
      Channel channel, ObjectMapper objectMapper, EventStoreRepository eventStoreRepository) {
    this.channel = channel;
    this.objectMapper = objectMapper;
    this.eventStoreRepository = eventStoreRepository;
  }

  @Override
  public void publish(Set<DomainEvent> events) {
    events.forEach(
        event -> {
          PublicEvent publicEvent = PublicEventFactory.from(event);
          try {
            eventStoreRepository.save(publicEvent);
            publishEvent(publicEvent, channel);

            logger.info("Event {} has been published", publicEvent.getType());
          } catch (Exception ex) {
            throw new UnpublishedDomainEvent(event, ex);
          }
        });
  }

  private void publishEvent(PublicEvent publicEvent, Channel channel) throws IOException {
    byte[] serializedEvent = objectMapper.writeValueAsBytes(publicEvent);
    String queueName = getQueueNameFor(publicEvent);
    channel.basicPublish("", queueName, null, serializedEvent);
  }

  // TODO: Simple mapping for events and queues. Update it
  private String getQueueNameFor(PublicEvent publicEvent) {
    return switch (publicEvent.getType()) {
      case "hotelmanagement.hotel.created" -> "hotelmanagement.hotel";
      case "hotelmanagement.review.created", "hotelmanagement.review.updated" ->
          "hotelmanagement.review";
      default -> "hotelmanagement.dlq";
    };
  }
}
