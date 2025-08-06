package org.egualpam.contexts.hotel.shared.infrastructure.helpers;

import static java.util.Objects.nonNull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Set;
import java.util.UUID;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public final class HotelTestRepository {

  private final ObjectMapper objectMapper;
  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  public HotelTestRepository(
      ObjectMapper objectMapper, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
    this.objectMapper = objectMapper;
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
        INSERT INTO hotel_rating(id, hotel_id, review_count, avg_value, reviews)
        VALUES (:id, :hotelId, :reviewCount, :averageValue, :reviewsJson::jsonb)
        """;

    Set<String> reviews = Set.of(UUID.randomUUID().toString());
    ReviewsTestDto reviewsTestDto = new ReviewsTestDto(reviews);

    final String reviewsAsString;
    try {
      reviewsAsString = objectMapper.writeValueAsString(reviewsTestDto);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("'reviews' could not be written as String", e);
    }

    MapSqlParameterSource queryParameters = new MapSqlParameterSource();
    queryParameters.addValue("id", UUID.randomUUID());
    queryParameters.addValue("hotelId", hotelId);
    queryParameters.addValue("ratingSum", averageRating);
    queryParameters.addValue("reviewCount", 1);
    queryParameters.addValue("averageValue", averageRating);
    queryParameters.addValue("reviewsJson", reviewsAsString);

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

  private record ReviewsTestDto(Set<String> reviews) {}
}
