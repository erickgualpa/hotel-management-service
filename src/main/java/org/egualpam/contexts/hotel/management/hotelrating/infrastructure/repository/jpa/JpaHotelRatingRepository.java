package org.egualpam.contexts.hotel.management.hotelrating.infrastructure.repository.jpa;

import static java.util.Objects.isNull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Optional;
import java.util.UUID;
import org.egualpam.contexts.hotel.management.hotelrating.domain.HotelRating;
import org.egualpam.contexts.hotel.shared.domain.AggregateId;
import org.egualpam.contexts.hotel.shared.domain.AggregateRepository;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public final class JpaHotelRatingRepository implements AggregateRepository<HotelRating> {

  private static final String insertIntoHotelRating =
      """
      INSERT INTO hotel_rating(id, hotel_id, review_count, avg_value, reviews)
      VALUES(:id, :hotelId, :reviewCount, :averageRating, :reviewsJson::jsonb)
      ON CONFLICT (id)
      DO UPDATE SET
        review_count=:reviewCount,
        avg_value=:averageRating,
        reviews=:reviewsJson::jsonb
      """;

  private final ObjectMapper objectMapper;
  private final EntityManager entityManager;
  private final NamedParameterJdbcTemplate jdbcTemplate;

  public JpaHotelRatingRepository(
      ObjectMapper objectMapper,
      EntityManager entityManager,
      NamedParameterJdbcTemplate jdbcTemplate) {
    this.objectMapper = objectMapper;
    this.entityManager = entityManager;
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public Optional<HotelRating> find(AggregateId id) {
    var hotelRatingId = UUID.fromString(id.value());

    var persistenceHotelRating = entityManager.find(PersistenceHotelRating.class, hotelRatingId);

    if (isNull(persistenceHotelRating)) {
      return Optional.empty();
    }

    var hotelRating =
        HotelRating.load(
            persistenceHotelRating.id().toString(),
            persistenceHotelRating.hotelId().toString(),
            persistenceHotelRating.persistenceReviews().reviews(),
            persistenceHotelRating.average());

    return Optional.of(hotelRating);
  }

  @Override
  public void save(HotelRating hotelRating) {
    var persistenceReviews = new PersistenceReviews(hotelRating.reviews());

    final String reviewsAsString;
    try {
      reviewsAsString = objectMapper.writeValueAsString(persistenceReviews);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Hotel rating could be saved", e);
    }

    var sqlParameterSource = new MapSqlParameterSource();
    sqlParameterSource.addValue("id", UUID.fromString(hotelRating.id().value()));
    sqlParameterSource.addValue("hotelId", UUID.fromString(hotelRating.hotelId()));
    sqlParameterSource.addValue("averageRating", hotelRating.average());
    sqlParameterSource.addValue("reviewCount", hotelRating.reviews().size());
    sqlParameterSource.addValue("reviewsJson", reviewsAsString);

    jdbcTemplate.update(insertIntoHotelRating, sqlParameterSource);
  }
}
