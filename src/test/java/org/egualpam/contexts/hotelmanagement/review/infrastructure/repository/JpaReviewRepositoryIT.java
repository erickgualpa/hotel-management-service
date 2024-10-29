package org.egualpam.contexts.hotelmanagement.review.infrastructure.repository;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;

import jakarta.persistence.EntityManager;
import java.util.Optional;
import org.egualpam.contexts.hotelmanagement.review.domain.Review;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateId;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateRepository;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class JpaReviewRepositoryIT extends AbstractIntegrationTest {

  @Autowired private EntityManager entityManager;

  @Test
  void returnEmptyOptional_whenNoReviewMatchesId() {
    final AggregateRepository<Review> testee = new JpaReviewRepository(entityManager);
    AggregateId reviewId = new AggregateId(randomUUID().toString());
    Optional<Review> result = testee.find(reviewId);
    assertThat(result).isEmpty();
  }
}
