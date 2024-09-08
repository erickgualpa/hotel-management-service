package org.egualpam.services.hotelmanagement.review.infrastructure.persistence.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.egualpam.services.hotelmanagement.review.domain.Review;
import org.egualpam.services.hotelmanagement.shared.domain.AggregateId;
import org.egualpam.services.hotelmanagement.shared.domain.AggregateRepository;
import org.egualpam.services.hotelmanagement.shared.infrastructure.persistence.jpa.PersistenceReview;

import java.util.Optional;
import java.util.UUID;

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
                        .setParameter("id", UUID.fromString(id.value()));

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
        persistenceReview.setId(UUID.fromString(review.getId().value()));
        persistenceReview.setHotelId(UUID.fromString(review.getHotelId().value()));
        persistenceReview.setRating(review.getRating().value());
        persistenceReview.setComment(review.getComment().value());

        entityManager.merge(persistenceReview);
        entityManager.flush();
    }
}