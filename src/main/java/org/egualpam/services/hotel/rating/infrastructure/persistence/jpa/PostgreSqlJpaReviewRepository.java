package org.egualpam.services.hotel.rating.infrastructure.persistence.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.egualpam.services.hotel.rating.domain.reviews.Review;
import org.egualpam.services.hotel.rating.domain.reviews.ReviewRepository;
import org.egualpam.services.hotel.rating.domain.shared.Identifier;

import java.util.List;
import java.util.UUID;

public final class PostgreSqlJpaReviewRepository extends ReviewRepository {

    private final EntityManager entityManager;

    public PostgreSqlJpaReviewRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Review> findByHotelIdentifier(Identifier hotelIdentifier) {
        Query query =
                entityManager
                        .createNativeQuery("""
                                        SELECT r.id, r.rating, r.comment, r.hotel_id
                                        FROM reviews r
                                        WHERE r.hotel_id = :hotel_id
                                        """,
                                org.egualpam.services.hotel.rating.infrastructure.persistence.jpa.Review.class)
                        .setParameter(
                                "hotel_id",
                                UUID.fromString(hotelIdentifier.value())
                        );

        List<org.egualpam.services.hotel.rating.infrastructure.persistence.jpa.Review> reviews =
                query.getResultList();

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
    public void save(Review review) {
        org.egualpam.services.hotel.rating.infrastructure.persistence.jpa.Review persistenceEntity =
                new org.egualpam.services.hotel.rating.infrastructure.persistence.jpa.Review();
        persistenceEntity.setId(UUID.fromString(review.getIdentifier().value()));
        persistenceEntity.setHotelId(UUID.fromString(review.getHotelIdentifier().value()));
        persistenceEntity.setRating(review.getRating().value());
        persistenceEntity.setComment(review.getComment().value());

        entityManager.persist(persistenceEntity);
        entityManager.flush();
    }
}