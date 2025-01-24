package org.egualpam.contexts.hotelmanagement.shared.infrastructure.eventbus.simple;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchemaFactory;
import java.util.Set;
import org.egualpam.contexts.hotelmanagement.shared.domain.DomainEvent;
import org.egualpam.contexts.hotelmanagement.shared.domain.EventBus;
import org.egualpam.contexts.hotelmanagement.shared.domain.UnpublishedDomainEvent;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.eventbus.events.PublicEvent;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.eventbus.shared.EventStoreRepository;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.eventbus.shared.PublicEventFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class SimpleEventBus implements EventBus {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private final ObjectMapper objectMapper;
  private final JsonSchemaFactory jsonSchemaFactory;
  private final EventStoreRepository eventStoreRepository;

  public SimpleEventBus(
      ObjectMapper objectMapper,
      JsonSchemaFactory jsonSchemaFactory,
      NamedParameterJdbcTemplate jdbcTemplate) {
    this.objectMapper = objectMapper;
    this.jsonSchemaFactory = jsonSchemaFactory;
    this.eventStoreRepository = new EventStoreRepository(objectMapper, jdbcTemplate);
  }

  @Override
  public void publish(Set<DomainEvent> events) {
    events.forEach(this::persistEvent);
  }

  private void persistEvent(DomainEvent domainEvent) {
    PublicEvent publicEvent =
        PublicEventFactory.mapAndValidate(domainEvent, objectMapper, jsonSchemaFactory);
    try {
      eventStoreRepository.save(publicEvent);
      logger.info("Event {} has been published", publicEvent.getType());
    } catch (RuntimeException e) {
      throw new UnpublishedDomainEvent(domainEvent, e);
    }
  }
}
