package org.egualpam.contexts.hotelmanagement.shared.infrastructure.eventbus.simple;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;
import org.egualpam.contexts.hotelmanagement.shared.domain.DomainEvent;
import org.egualpam.contexts.hotelmanagement.shared.domain.EventBus;
import org.egualpam.contexts.hotelmanagement.shared.domain.UnpublishedDomainEvent;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.eventbus.events.PublicEvent;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.eventbus.events.PublicEventFactory;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class SimpleEventBus implements EventBus {

  private static final String INSERT_INTO_EVENT_STORE =
      """
      INSERT INTO event_store(id, aggregate_id, occurred_on, event_type, event_version, event_payload)
      VALUES (:id, :aggregateId, :occurredOn, :eventType, :eventVersion, :eventPayload::jsonb)
      """;

  private final ObjectMapper objectMapper;
  private final NamedParameterJdbcTemplate jdbcTemplate;

  public SimpleEventBus(ObjectMapper objectMapper, NamedParameterJdbcTemplate jdbcTemplate) {
    this.objectMapper = objectMapper;
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public void publish(Set<DomainEvent> events) {
    events.forEach(this::persistEvent);
  }

  private void persistEvent(DomainEvent domainEvent) {
    PublicEvent publicEvent = PublicEventFactory.from(domainEvent);

    final String eventAsString;
    try {
      eventAsString = objectMapper.writeValueAsString(publicEvent);
    } catch (JsonProcessingException e) {
      throw new UnpublishedDomainEvent(domainEvent, e);
    }

    MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
    sqlParameterSource.addValue("id", UUID.fromString(publicEvent.getId()));
    sqlParameterSource.addValue("aggregateId", UUID.fromString(publicEvent.getAggregateId()));
    sqlParameterSource.addValue("occurredOn", Timestamp.from(publicEvent.getOccurredOn()));
    sqlParameterSource.addValue("eventType", publicEvent.getType());
    sqlParameterSource.addValue("eventVersion", publicEvent.getVersion());
    sqlParameterSource.addValue("eventPayload", eventAsString);

    jdbcTemplate.update(INSERT_INTO_EVENT_STORE, sqlParameterSource);
  }
}
