package org.egualpam.services.hotelmanagement.reviews.infrastructure.persistence.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.egualpam.services.hotelmanagement.domain.reviews.Review;
import org.egualpam.services.hotelmanagement.domain.shared.AggregateId;
import org.egualpam.services.hotelmanagement.domain.shared.AggregateRepository;
import org.egualpam.services.hotelmanagement.shared.infrastructure.persistence.jpa.PersistenceReview;

import java.util.Optional;

public class PostgreSqlJpaReviewRepository implements AggregateRepository<Review> {

    private final EntityManager entityManager;

    public PostgreSqlJpaReviewRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Review> find(AggregateId id) {
        String sql = """
                SELECT r.id, r.rating, r.comment, r.hotel_id
                FROM reviews r
                WHERE r.id = :id
                """;

        Query query =
                entityManager
                        .createNativeQuery(sql, PersistenceReview.class)
                        .setParameter("id", id.value());

        final PersistenceReview persistenceReview;
        try {
            persistenceReview = (PersistenceReview) query.getSingleResult();
        } catch (NoResultException e) {
            return Optional.empty();
        }

        Review review = new Review(
                persistenceReview.getId().toString(),
                persistenceReview.getHotelId().toString(),
                persistenceReview.getRating(),
                persistenceReview.getComment()
        );

        return Optional.of(review);
    }

    @Override
    @Transactional
    public void save(Review review) {
        PersistenceReview persistenceReview = new PersistenceReview();
        persistenceReview.setId(review.getId().value());
        persistenceReview.setHotelId(review.getHotelId().value());
        persistenceReview.setRating(review.getRating().value());
        persistenceReview.setComment(review.getComment().value());

        entityManager.merge(persistenceReview);
        entityManager.flush();
    }
}