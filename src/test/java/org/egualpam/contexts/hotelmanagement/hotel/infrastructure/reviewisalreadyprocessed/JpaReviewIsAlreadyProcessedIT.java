package org.egualpam.contexts.hotelmanagement.hotel.infrastructure.reviewisalreadyprocessed;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.persistence.EntityManager;
import org.egualpam.contexts.hotelmanagement.hotel.domain.ReviewIsAlreadyProcessed;
import org.egualpam.contexts.hotelmanagement.shared.domain.EntityId;
import org.egualpam.contexts.hotelmanagement.shared.domain.UniqueId;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.AbstractIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.support.TransactionTemplate;

class JpaReviewIsAlreadyProcessedIT extends AbstractIntegrationTest {

  @Autowired private TransactionTemplate transactionTemplate;
  @Autowired private EntityManager entityManager;

  private ReviewIsAlreadyProcessed testee;

  @BeforeEach
  void setUp() {
    testee = new JpaReviewIsAlreadyProcessed(entityManager);
  }

  @Test
  void returnTrue_whenReviewIdIsCheckedMoreThanOnce() {
    EntityId reviewId = new EntityId(UniqueId.get().value());
    transactionTemplate.executeWithoutResult(
        ts -> {
          boolean first = testee.with(reviewId);
          boolean second = testee.with(reviewId);
          assertFalse(first);
          assertTrue(second);
        });
  }
}
