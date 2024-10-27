package org.egualpam.contexts.hotelmanagement.review.infrastructure.persistence.jpa;

import static org.junit.jupiter.api.Assertions.assertThrows;

import jakarta.persistence.EntityManager;
import org.egualpam.contexts.hotelmanagement.review.application.query.ManyReviews;
import org.egualpam.contexts.hotelmanagement.review.domain.ReviewCriteria;
import org.egualpam.contexts.hotelmanagement.shared.application.query.ReadModelSupplier;
import org.egualpam.contexts.hotelmanagement.shared.domain.Criteria;
import org.egualpam.contexts.hotelmanagement.shared.domain.exceptions.RequiredPropertyIsMissing;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PostgreSqlJpaManyReviewsSupplierShould {

  @Mock private EntityManager entityManager;

  private ReadModelSupplier<ManyReviews> testee;

  @BeforeEach
  void setUp() {
    testee = new PostgreSqlJpaManyReviewsReadModelSupplier(entityManager);
  }

  @Test
  void throwDomainException_whenHotelIdIsMissingInCriteria() {
    Criteria reviewCriteria = new ReviewCriteria(null);
    assertThrows(RequiredPropertyIsMissing.class, () -> testee.get(reviewCriteria));
  }
}
