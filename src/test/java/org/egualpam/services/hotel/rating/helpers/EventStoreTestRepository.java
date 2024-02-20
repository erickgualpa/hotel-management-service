package org.egualpam.services.hotel.rating.helpers;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import static java.util.Objects.nonNull;

public class EventStoreTestRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public EventStoreTestRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public boolean domainEventExists(String eventType) {
        String sql = """
                SELECT COUNT(*)
                FROM event_store
                WHERE event_type = :eventType
                """;

        MapSqlParameterSource queryParameters = new MapSqlParameterSource();
        queryParameters.addValue("eventType", eventType);

        Integer count = namedParameterJdbcTemplate.queryForObject(
                sql,
                queryParameters,
                Integer.class
        );

        return nonNull(count) && count == 1;
    }
}
