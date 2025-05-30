package org.egualpam.contexts.hotelmanagement.shared.infrastructure.helpers;

import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class EventStoreTestRepository {

  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  public EventStoreTestRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
    this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
  }

  public PublicEventResult findEvent(String eventId) {
    String sql =
        """
            SELECT event_type, event_version, aggregate_id, occurred_on
            FROM event_store
            WHERE id = :eventId
            """;

    MapSqlParameterSource queryParameters = new MapSqlParameterSource();
    queryParameters.addValue("eventId", UUID.fromString(eventId));

    try {
      return namedParameterJdbcTemplate.queryForObject(
          sql,
          queryParameters,
          (rs, rowNum) ->
              new PublicEventResult(
                  eventId,
                  rs.getString("event_type"),
                  rs.getString("event_version"),
                  rs.getString("aggregate_id"),
                  rs.getTimestamp("occurred_on").toInstant()));
    } catch (EmptyResultDataAccessException e) {
      Assertions.fail("Event with id [%s] was not found".formatted(eventId));
      return null;
    }
  }
}
