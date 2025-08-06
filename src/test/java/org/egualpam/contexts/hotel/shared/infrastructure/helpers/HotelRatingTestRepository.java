package org.egualpam.contexts.hotel.shared.infrastructure.helpers;

import static java.util.Objects.nonNull;

import java.util.UUID;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public final class HotelRatingTestRepository {

  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  public HotelRatingTestRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
    this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
  }

  public boolean hotelRatingIsInitialized(UUID hotelId) {
    String query =
        """
        SELECT COUNT(*)
        FROM hotel_rating
        WHERE hotel_id = :hotelId
        """;

    MapSqlParameterSource queryParameters = new MapSqlParameterSource();
    queryParameters.addValue("hotelId", hotelId);

    Integer count =
        namedParameterJdbcTemplate.queryForObject(query, queryParameters, Integer.class);

    return nonNull(count) && count == 1;
  }
}
