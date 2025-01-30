package org.egualpam.contexts.hotelmanagement.hotelrating.infrastructure.consumer;

import java.util.UUID;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

class GetHotelRatingId {

  private final NamedParameterJdbcTemplate jdbcTemplate;

  GetHotelRatingId(NamedParameterJdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  String fromHotel(String hotelId) {
    String sql =
        """
        SELECT id
        FROM hotel_rating
        WHERE hotel_id = :hotelId
        """;

    MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
    sqlParameterSource.addValue("hotelId", UUID.fromString(hotelId));

    return jdbcTemplate.queryForObject(sql, sqlParameterSource, String.class);
  }
}
