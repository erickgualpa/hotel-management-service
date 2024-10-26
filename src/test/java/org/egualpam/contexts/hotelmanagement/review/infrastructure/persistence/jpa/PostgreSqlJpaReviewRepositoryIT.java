package org.egualpam.contexts.hotelmanagement.review.infrastructure.persistence.jpa;

import jakarta.persistence.EntityManager;
import org.egualpam.contexts.hotelmanagement.review.domain.Review;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateId;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateRepository;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;

class PostgreSqlJpaReviewRepositoryIT extends AbstractIntegrationTest {

    @Autowired
    private EntityManager entityManager;

    @Test
    void returnEmptyOptional_whenNoReviewMatchesId() {
        final AggregateRepository<Review> testee = new PostgreSqlJpaReviewRepository(entityManager);
        AggregateId reviewId = new AggregateId(randomUUID().toString());
        Optional<Review> result = testee.find(reviewId);
        assertThat(result).isEmpty();
    }
}