package org.egualpam.contexts.hotelmanagement.shared.infrastructure.eventbus.shared;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Timestamp;
import java.util.UUID;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.eventbus.events.PublicEvent;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class EventStoreRepository {

  private static final String insertIntoEventStore =
      """
      INSERT INTO event_store(id, aggregate_id, occurred_on, event_type, event_version, event_payload)
      VALUES (:id, :aggregateId, :occurredOn, :eventType, :eventVersion, :eventPayload::jsonb)
      """;

  private final ObjectMapper objectMapper;
  private final NamedParameterJdbcTemplate jdbcTemplate;

  public EventStoreRepository(ObjectMapper objectMapper, NamedParameterJdbcTemplate jdbcTemplate) {
    this.objectMapper = objectMapper;
    this.jdbcTemplate = jdbcTemplate;
  }

  public void save(PublicEvent publicEvent) {
    final String eventAsString;
    try {
      eventAsString = objectMapper.writeValueAsString(publicEvent);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Event could not be saved in event store", e);
    }

    MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
    sqlParameterSource.addValue("id", UUID.fromString(publicEvent.getId()));
    sqlParameterSource.addValue("aggregateId", UUID.fromString(publicEvent.getAggregateId()));
    sqlParameterSource.addValue("occurredOn", Timestamp.from(publicEvent.getOccurredOn()));
    sqlParameterSource.addValue("eventType", publicEvent.getType());
    sqlParameterSource.addValue("eventVersion", publicEvent.getVersion());
    sqlParameterSource.addValue("eventPayload", eventAsString);

    jdbcTemplate.update(insertIntoEventStore, sqlParameterSource);
  }
}
