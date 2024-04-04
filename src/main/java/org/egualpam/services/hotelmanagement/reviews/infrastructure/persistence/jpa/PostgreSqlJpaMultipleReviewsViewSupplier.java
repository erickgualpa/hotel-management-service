package org.egualpam.services.hotelmanagement.reviews.infrastructure.persistence.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.egualpam.services.hotelmanagement.reviews.application.query.MultipleReviewsView;
import org.egualpam.services.hotelmanagement.reviews.domain.HotelId;
import org.egualpam.services.hotelmanagement.reviews.domain.ReviewCriteria;
import org.egualpam.services.hotelmanagement.shared.application.query.ViewSupplier;
import org.egualpam.services.hotelmanagement.shared.domain.Criteria;
import org.egualpam.services.hotelmanagement.shared.domain.exception.RequiredPropertyIsMissing;
import org.egualpam.services.hotelmanagement.shared.infrastructure.persistence.jpa.PersistenceReview;

import java.util.List;
import java.util.UUID;

public class PostgreSqlJpaMultipleReviewsViewSupplier implements ViewSupplier<MultipleReviewsView> {

    private final EntityManager entityManager;

    public PostgreSqlJpaMultipleReviewsViewSupplier(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public MultipleReviewsView get(Criteria criteria) {
        ReviewCriteria reviewCriteria = (ReviewCriteria) criteria;
        HotelId hotelId = reviewCriteria.getHotelId().orElseThrow(RequiredPropertyIsMissing::new);

        String sql = """
                SELECT r.id, r.rating, r.comment, r.hotel_id
                FROM reviews r
                WHERE r.hotel_id = :hotel_id
                """;

        Query query =
                entityManager
                        .createNativeQuery(sql, PersistenceReview.class)
                        .setParameter("hotel_id", UUID.fromString(hotelId.value()));

        List<PersistenceReview> persistenceReviews = query.getResultList();

        List<MultipleReviewsView.Review> reviews = persistenceReviews.stream()
                .map(
                        review ->
                                new MultipleReviewsView.Review(
                                        review.getRating(),
                                        review.getComment()))
                .toList();

        return new MultipleReviewsView(reviews);
    }
}
