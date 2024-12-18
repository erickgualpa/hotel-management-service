package org.egualpam.contexts.hotelmanagement.shared.infrastructure.eventbus.springamqp;

import static org.slf4j.LoggerFactory.getLogger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Set;
import org.egualpam.contexts.hotelmanagement.shared.domain.DomainEvent;
import org.egualpam.contexts.hotelmanagement.shared.domain.EventBus;
import org.egualpam.contexts.hotelmanagement.shared.domain.UnpublishedDomainEvent;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.eventbus.events.PublicEvent;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.eventbus.events.PublicEventFactory;
import org.slf4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

public final class SpringAmqpEventBus implements EventBus {

  private final ObjectMapper objectMapper;
  private final RabbitTemplate rabbitTemplate;
  private final Logger logger = getLogger(this.getClass());

  public SpringAmqpEventBus(ObjectMapper objectMapper, RabbitTemplate rabbitTemplate) {
    this.objectMapper = objectMapper;
    this.rabbitTemplate = rabbitTemplate;
  }

  @Override
  public void publish(Set<DomainEvent> events) {
    events.forEach(
        event -> {
          PublicEvent publicEvent = PublicEventFactory.from(event);
          try {
            publishEvent(publicEvent);
          } catch (Exception e) {
            throw new UnpublishedDomainEvent(event, e);
          }
        });
  }

  private void publishEvent(PublicEvent event) throws JsonProcessingException {
    final byte[] bytesFromEvent;
    bytesFromEvent = objectMapper.writeValueAsBytes(event);
    rabbitTemplate.convertAndSend(event.getType(), bytesFromEvent);
    logger.info("Event {} has been published", event.getType());
  }
}
