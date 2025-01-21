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
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.eventbus.shared.EventStoreRepository;
import org.slf4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

public final class SpringAmqpEventBus implements EventBus {

  private final ObjectMapper objectMapper;
  private final EventStoreRepository eventStoreRepository;
  private final RabbitTemplate rabbitTemplate;
  private final Logger logger = getLogger(this.getClass());

  public SpringAmqpEventBus(
      ObjectMapper objectMapper,
      EventStoreRepository eventStoreRepository,
      RabbitTemplate rabbitTemplate) {
    this.objectMapper = objectMapper;
    this.eventStoreRepository = eventStoreRepository;
    this.rabbitTemplate = rabbitTemplate;
  }

  @Override
  public void publish(Set<DomainEvent> events) {
    events.forEach(
        event -> {
          PublicEvent publicEvent = PublicEventFactory.from(event);
          try {
            eventStoreRepository.save(publicEvent);
            publishEvent(publicEvent);

            logger.info("Event {} has been published", publicEvent.getType());
          } catch (Exception e) {
            throw new UnpublishedDomainEvent(event, e);
          }
        });
  }

  private void publishEvent(PublicEvent event) throws JsonProcessingException {
    final byte[] bytesFromEvent = objectMapper.writeValueAsBytes(event);
    rabbitTemplate.convertAndSend(event.getType(), bytesFromEvent);
  }
}
