package org.egualpam.contexts.hotelmanagement.shared.infrastructure.helpers;

import static java.util.Objects.nonNull;

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

  public void insertHotelAverageRating(UUID hotelId, Double averageRating) {
    String query =
        """
        INSERT INTO hotel_average_rating(hotel_id, rating_sum, review_count, avg_value)
        VALUES
            (:hotelId, :ratingSum, :reviewCount, :averageValue)
        """;

    MapSqlParameterSource queryParameters = new MapSqlParameterSource();
    queryParameters.addValue("hotelId", hotelId);
    queryParameters.addValue("ratingSum", averageRating);
    queryParameters.addValue("reviewCount", 1);
    queryParameters.addValue("averageValue", averageRating);

    namedParameterJdbcTemplate.update(query, queryParameters);
  }

  public boolean hotelExists(UUID hotelId) {
    String query =
        """
        SELECT COUNT(*)
        FROM hotels
        WHERE id = :hotelId
        """;

    MapSqlParameterSource queryParameters = new MapSqlParameterSource();
    queryParameters.addValue("hotelId", hotelId);

    Integer count =
        namedParameterJdbcTemplate.queryForObject(query, queryParameters, Integer.class);

    return nonNull(count) && count == 1;
  }
}
