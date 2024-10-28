package org.egualpam.contexts.hotelmanagement.hotel.infrastructure.readmodelsupplier.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import java.math.BigDecimal;
import java.util.UUID;

class GetHotelAverageRating {

  private static final String findAverageRatingByHotelId =
      """
          SELECT avg_value
          FROM hotel_average_rating
          WHERE hotel_id=:hotelId
          """;

  private final EntityManager entityManager;

  GetHotelAverageRating(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  HotelAverageRating using(UUID hotelId) {
    BigDecimal hotelAverageRating;
    try {
      hotelAverageRating =
          (BigDecimal)
              entityManager
                  .createNativeQuery(findAverageRatingByHotelId)
                  .setParameter("hotelId", hotelId)
                  .getSingleResult();
    } catch (NoResultException e) {
      hotelAverageRating = BigDecimal.ZERO;
    }
    return new HotelAverageRating(hotelAverageRating.doubleValue());
  }
}

record HotelAverageRating(Double value) {}
