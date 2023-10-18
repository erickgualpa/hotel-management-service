package org.egualpam.services.hotel.rating.infrastructure.persistence.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.egualpam.services.hotel.rating.domain.Review;
import org.egualpam.services.hotel.rating.domain.ReviewRepository;

import java.util.List;
import java.util.UUID;

public class PostgreSqlJpaReviewRepository extends ReviewRepository {

    private final EntityManager entityManager;

    public PostgreSqlJpaReviewRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Review> findByHotelIdentifier(String hotelIdentifier) {
        Query query =
                entityManager
                        .createNativeQuery("""
                                        SELECT r.id, r.rating, r.comment, r.hotel_id
                                        FROM reviews r
                                        WHERE r.hotel_id = :hotel_id
                                        """,
                                org.egualpam.services.hotel.rating.infrastructure.persistence.jpa.Review.class)
                        .setParameter("hotel_id", UUID.fromString(hotelIdentifier));

        List<org.egualpam.services.hotel.rating.infrastructure.persistence.jpa.Review> reviews =
                query.getResultList();

        return reviews.stream()
                .map(review ->
                        mapIntoEntity(
                                review.getId().toString(),
                                review.getRating(),
                                review.getComment()
                        )
                ).toList();
    }
}