package org.egualpam.contexts.hotelmanagement.review.infrastructure.readmodelsupplier;

import static org.junit.jupiter.api.Assertions.assertThrows;

import jakarta.persistence.EntityManager;
import org.egualpam.contexts.hotel.shared.application.query.ReadModelSupplier;
import org.egualpam.contexts.hotel.shared.domain.RequiredPropertyIsMissing;
import org.egualpam.contexts.hotelmanagement.review.application.query.ManyReviews;
import org.egualpam.contexts.hotelmanagement.review.domain.ReviewCriteria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class JpaManyReviewsReadModelSupplierShould {

  @Mock private EntityManager entityManager;

  private ReadModelSupplier<ReviewCriteria, ManyReviews> testee;

  @BeforeEach
  void setUp() {
    testee = new JpaManyReviewsReadModelSupplier(entityManager);
  }

  @Test
  void throwDomainException_whenHotelIdIsMissingInCriteria() {
    ReviewCriteria reviewCriteria = new ReviewCriteria(null);
    assertThrows(RequiredPropertyIsMissing.class, () -> testee.get(reviewCriteria));
  }
}
