package org.egualpam.contexts.hotelmanagement.shared.infrastructure.helpers;

import java.util.UUID;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public final class HotelTestRepository {

  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  public HotelTestRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
    this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
  }

  public void insertHotel(
      UUID hotelIdentifier,
      String hotelName,
      String hotelDescription,
      String hotelLocation,
      Integer price,
      String imageURL) {
    String query =
        """
                INSERT INTO hotels(id, name, description, location, price, image_url)
                VALUES
                    (:hotelIdentifier, :hotelName, :hotelDescription, :hotelLocation, :price, :imageURL)
                """;

    MapSqlParameterSource queryParameters = new MapSqlParameterSource();
    queryParameters.addValue("hotelIdentifier", hotelIdentifier);
    queryParameters.addValue("hotelName", hotelName);
    queryParameters.addValue("hotelDescription", hotelDescription);
    queryParameters.addValue("hotelLocation", hotelLocation);
    queryParameters.addValue("price", price);
    queryParameters.addValue("imageURL", imageURL);

    namedParameterJdbcTemplate.update(query, queryParameters);
  }
}
