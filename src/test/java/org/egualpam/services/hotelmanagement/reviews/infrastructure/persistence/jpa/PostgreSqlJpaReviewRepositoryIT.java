package org.egualpam.services.hotelmanagement.reviews.infrastructure.persistence.jpa;

import jakarta.persistence.EntityManager;
import org.egualpam.services.hotelmanagement.domain.reviews.Review;
import org.egualpam.services.hotelmanagement.domain.shared.AggregateId;
import org.egualpam.services.hotelmanagement.domain.shared.AggregateRepository;
import org.egualpam.services.hotelmanagement.shared.infrastructure.AbstractIntegrationTest;
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
        AggregateId reviewId = new AggregateId(randomUUID());
        Optional<Review> result = testee.find(reviewId);
        assertThat(result).isEmpty();
    }
}