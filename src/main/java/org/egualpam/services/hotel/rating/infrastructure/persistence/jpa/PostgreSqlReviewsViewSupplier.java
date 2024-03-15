package org.egualpam.services.hotel.rating.infrastructure.persistence.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.egualpam.services.hotel.rating.application.reviews.ReviewsView;
import org.egualpam.services.hotel.rating.application.shared.ViewSupplier;
import org.egualpam.services.hotel.rating.domain.reviews.ReviewCriteria;
import org.egualpam.services.hotel.rating.domain.shared.Criteria;

import java.util.List;
import java.util.UUID;

public class PostgreSqlReviewsViewSupplier implements ViewSupplier<ReviewsView> {

    private final EntityManager entityManager;

    public PostgreSqlReviewsViewSupplier(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public ReviewsView get(Criteria criteria) {
        UUID hotelId = ((ReviewCriteria) criteria).getHotelId().value();

        String sql = """
                SELECT r.id, r.rating, r.comment, r.hotel_id
                FROM reviews r
                WHERE r.hotel_id = :hotel_id
                """;

        Query query =
                entityManager
                        .createNativeQuery(sql, PersistenceReview.class)
                        .setParameter("hotel_id", hotelId);

        List<PersistenceReview> persistenceReviews = query.getResultList();

        List<ReviewsView.Review> reviews = persistenceReviews.stream()
                .map(
                        review ->
                                new ReviewsView.Review(
                                        review.getRating(),
                                        review.getComment()))
                .toList();

        return new ReviewsView(reviews);
    }
}
