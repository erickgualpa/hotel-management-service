package org.egualpam.contexts.hotelmanagement.review.infrastructure.persistence.jpa;

import jakarta.persistence.EntityManager;
import org.egualpam.contexts.hotelmanagement.review.application.query.MultipleReviewsView;
import org.egualpam.contexts.hotelmanagement.review.domain.ReviewCriteria;
import org.egualpam.contexts.hotelmanagement.shared.application.query.ViewSupplier;
import org.egualpam.contexts.hotelmanagement.shared.domain.Criteria;
import org.egualpam.contexts.hotelmanagement.shared.domain.exceptions.RequiredPropertyIsMissing;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class PostgreSqlJpaMultipleReviewsViewSupplierShould {

    @Mock
    private EntityManager entityManager;

    private ViewSupplier<MultipleReviewsView> testee;

    @BeforeEach
    void setUp() {
        testee = new PostgreSqlJpaMultipleReviewsViewSupplier(entityManager);
    }

    @Test
    void throwDomainException_whenHotelIdIsMissingInCriteria() {
        Criteria reviewCriteria = new ReviewCriteria(null);
        assertThrows(RequiredPropertyIsMissing.class, () -> testee.get(reviewCriteria));
    }
}
