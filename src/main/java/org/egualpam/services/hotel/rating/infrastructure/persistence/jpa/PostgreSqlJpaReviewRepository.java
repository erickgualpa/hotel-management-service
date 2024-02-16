package org.egualpam.services.hotel.rating.infrastructure.persistence.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.egualpam.services.hotel.rating.domain.reviews.Review;
import org.egualpam.services.hotel.rating.domain.reviews.ReviewRepository;
import org.egualpam.services.hotel.rating.domain.shared.Identifier;

import java.util.List;
import java.util.UUID;

public class PostgreSqlJpaReviewRepository extends ReviewRepository {

    private final EntityManager entityManager;

    public PostgreSqlJpaReviewRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Review> findByHotelIdentifier(Identifier hotelIdentifier) {
        Query query =
                entityManager
                        .createNativeQuery("""
                                        SELECT r.id, r.rating, r.comment, r.hotel_id
                                        FROM reviews r
                                        WHERE r.hotel_id = :hotel_id
                                        """,
                                PersistenceReview.class)
                        .setParameter(
                                "hotel_id",
                                UUID.fromString(hotelIdentifier.value())
                        );

        List<PersistenceReview> reviews = query.getResultList();

        return reviews.stream()
                .map(
                        review ->
                                mapIntoEntity(
                                        review.getId().toString(),
                                        review.getHotelId().toString(),
                                        review.getRating(),
                                        review.getComment()
                                )
                )
                .toList();
    }

    @Override
    @Transactional
    public void save(Review review) {
        PersistenceReview persistenceReview = new PersistenceReview();
        persistenceReview.setId(UUID.fromString(review.getIdentifier().value()));
        persistenceReview.setHotelId(UUID.fromString(review.getHotelIdentifier().value()));
        persistenceReview.setRating(review.getRating().value());
        persistenceReview.setComment(review.getComment().value());

        entityManager.persist(persistenceReview);
        entityManager.flush();
    }
}