package org.egualpam.contexts.hotelmanagement.shared.infrastructure.eventbus.springamqp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Set;
import org.egualpam.contexts.hotelmanagement.shared.domain.DomainEvent;
import org.egualpam.contexts.hotelmanagement.shared.domain.EventBus;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.eventbus.events.PublicEvent;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.eventbus.events.PublicEventFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

public final class SpringAmqpEventBus implements EventBus {

  private final ObjectMapper objectMapper;
  private final RabbitTemplate rabbitTemplate;

  private final Logger logger = LoggerFactory.getLogger(SpringAmqpEventBus.class);

  public SpringAmqpEventBus(ObjectMapper objectMapper, RabbitTemplate rabbitTemplate) {
    this.objectMapper = objectMapper;
    this.rabbitTemplate = rabbitTemplate;
  }

  @Override
  public void publish(Set<DomainEvent> events) {
    List<PublicEvent> publicEvents = events.stream().map(PublicEventFactory::from).toList();
    publicEvents.forEach(this::publishEvent);
  }

  private void publishEvent(PublicEvent event) {
    final byte[] bytesFromEvent;
    try {
      bytesFromEvent = objectMapper.writeValueAsBytes(event);
    } catch (JsonProcessingException e) {
      // TODO: Consider using a custom exception
      throw new RuntimeException("Domain event could not be sent", e);
    }
    rabbitTemplate.convertAndSend(event.getType(), bytesFromEvent);
    logger.info("Event {} has been published", event.getType());
  }
}
