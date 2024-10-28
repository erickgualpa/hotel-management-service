package org.egualpam.contexts.hotelmanagement.hotel.infrastructure.consumer;

import static java.util.Objects.nonNull;

import java.util.UUID;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.internaleventbus.spring.ReviewCreatedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class ReviewCreatedInternalEventConsumer implements ApplicationListener<ReviewCreatedEvent> {

  private final NamedParameterJdbcTemplate jdbcTemplate;

  public ReviewCreatedInternalEventConsumer(NamedParameterJdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public void onApplicationEvent(ReviewCreatedEvent event) {
    String query =
        """
        SELECT rating_sum, review_count
        FROM hotel_average_rating
        WHERE hotel_id=:hotelId
        """;
    MapSqlParameterSource queryParameters = new MapSqlParameterSource();
    queryParameters.addValue("hotelId", UUID.fromString(event.hotelId()));

    RowMapper<HotelAverageRating> rowMapper =
        (rs, rn) -> {
          int ratingSum = rs.getInt("rating_sum");
          int reviewCount = rs.getInt("review_count");
          return new HotelAverageRating(ratingSum, reviewCount);
        };

    HotelAverageRating hotelAverageRating;
    try {
      hotelAverageRating = jdbcTemplate.queryForObject(query, queryParameters, rowMapper);
    } catch (EmptyResultDataAccessException e) {
      hotelAverageRating = new HotelAverageRating(0, 0);
    }

    int ratingSum = nonNull(hotelAverageRating) ? hotelAverageRating.ratingSum() : 0;
    int reviewCount = nonNull(hotelAverageRating) ? hotelAverageRating.ratingCount() : 0;

    int updatedRatingSum = ratingSum + event.rating();
    int updatedReviewCount = reviewCount + 1;
    double averageRating = (double) updatedRatingSum / updatedReviewCount;

    String update =
        """
        INSERT INTO hotel_average_rating(hotel_id, rating_sum, review_count, avg_value)
        VALUES(:hotelId, :ratingSum, :reviewCount, :averageRating)
        ON CONFLICT (hotel_id)
        DO UPDATE SET
          rating_sum=:ratingSum,
          review_count=:reviewCount,
          avg_value=:averageRating
        """;

    MapSqlParameterSource updateParameters = new MapSqlParameterSource();
    updateParameters.addValue("hotelId", UUID.fromString(event.hotelId()));
    updateParameters.addValue("ratingSum", updatedRatingSum);
    updateParameters.addValue("reviewCount", updatedReviewCount);
    updateParameters.addValue("averageRating", averageRating);

    jdbcTemplate.update(update, updateParameters);
  }

  record HotelAverageRating(int ratingSum, int ratingCount) {}
}
