package org.egualpam.contexts.hotelmanagement.hotel.infrastructure.shared.jpa.hotelaveragerating;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import java.math.BigDecimal;
import java.util.UUID;

public class GetHotelAverageRating {

  private static final String findAverageRatingByHotelId =
      """
          SELECT review_count, avg_value
          FROM hotel_rating
          WHERE hotel_id=:hotelId
          """;

  private final EntityManager entityManager;

  public GetHotelAverageRating(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  public HotelAverageRating using(UUID hotelId) {
    HotelAverageRating hotelAverageRating;
    try {
      hotelAverageRating =
          (HotelAverageRating)
              entityManager
                  .createNativeQuery(findAverageRatingByHotelId, HotelAverageRating.class)
                  .setParameter("hotelId", hotelId)
                  .getSingleResult();
    } catch (NoResultException e) {
      hotelAverageRating = new HotelAverageRating(0, BigDecimal.ZERO);
    }
    return hotelAverageRating;
  }
}
