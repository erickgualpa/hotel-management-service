package org.egualpam.contexts.hotelmanagement.shared.infrastructure.helpers;

import java.util.UUID;
import org.egualpam.contexts.hotelmanagement.e2e.models.PublicEventResult;
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
                SELECT event_type, aggregate_id, occurred_on
                FROM event_store
                WHERE id = :eventId
                """;

    MapSqlParameterSource queryParameters = new MapSqlParameterSource();
    queryParameters.addValue("eventId", UUID.fromString(eventId));

    return namedParameterJdbcTemplate.queryForObject(
        sql,
        queryParameters,
        (rs, rowNum) ->
            new PublicEventResult(
                rs.getString("event_type"),
                rs.getString("aggregate_id"),
                rs.getTimestamp("occurred_on").toInstant()));
  }
}
