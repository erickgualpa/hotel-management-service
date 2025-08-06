package org.egualpam.contexts.hotel.customer.review.infrastructure.repository;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;

import jakarta.persistence.EntityManager;
import java.util.Optional;
import org.egualpam.contexts.AbstractIntegrationTest;
import org.egualpam.contexts.hotel.customer.review.domain.Review;
import org.egualpam.contexts.hotel.shared.domain.AggregateId;
import org.egualpam.contexts.hotel.shared.domain.AggregateRepository;
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
