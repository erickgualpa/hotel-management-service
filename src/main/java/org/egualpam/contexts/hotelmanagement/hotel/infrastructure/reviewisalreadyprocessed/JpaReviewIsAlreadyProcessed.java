package org.egualpam.contexts.hotelmanagement.hotel.infrastructure.reviewisalreadyprocessed;

import jakarta.persistence.EntityManager;
import java.util.UUID;
import org.egualpam.contexts.hotelmanagement.hotel.domain.ReviewIsAlreadyProcessed;
import org.egualpam.contexts.hotelmanagement.shared.domain.EntityId;

public final class JpaReviewIsAlreadyProcessed implements ReviewIsAlreadyProcessed {

  private static final String findReviewByIdQuery =
      """
      SELECT COUNT(*)
      FROM hotel_reviews_already_processed
      WHERE review_id=:reviewId
      """;

  private static final String insertReviewIdIntoTable =
      """
      INSERT INTO hotel_reviews_already_processed(review_id)
      VALUES(:reviewId)
      """;

  private final EntityManager entityManager;

  public JpaReviewIsAlreadyProcessed(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Override
  public boolean with(EntityId reviewId) {
    UUID reviewIdValue = UUID.fromString(reviewId.value());

    final Integer result =
        (Integer)
            entityManager
                .createNativeQuery(findReviewByIdQuery, Integer.class)
                .setParameter("reviewId", reviewIdValue)
                .getSingleResult();

    if (result == 0) {
      entityManager
          .createNativeQuery(insertReviewIdIntoTable)
          .setParameter("reviewId", reviewIdValue)
          .executeUpdate();
    }

    return result == 1;
  }
}
