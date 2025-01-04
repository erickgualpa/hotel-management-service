package org.egualpam.contexts.hotelmanagement.hotel.infrastructure.reviewisalreadyprocessed;

import jakarta.persistence.EntityManager;
import java.util.UUID;
import org.egualpam.contexts.hotelmanagement.hotel.domain.ReviewIsAlreadyProcessed;
import org.egualpam.contexts.hotelmanagement.shared.domain.EntityId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class JpaReviewIsAlreadyProcessed implements ReviewIsAlreadyProcessed {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

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
      // TODO: Check if this can be done using domain events
      entityManager
          .createNativeQuery(insertReviewIdIntoTable)
          .setParameter("reviewId", reviewIdValue)
          .executeUpdate();
    }

    boolean isReviewAlreadyProcessed = result == 1;

    if (isReviewAlreadyProcessed) {
      logger.warn("Review with id [%s] was already processed".formatted(reviewId.value()));
    }

    return isReviewAlreadyProcessed;
  }
}
