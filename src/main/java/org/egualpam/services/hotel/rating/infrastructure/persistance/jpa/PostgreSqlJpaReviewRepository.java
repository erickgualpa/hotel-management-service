package org.egualpam.services.hotel.rating.infrastructure.persistance.jpa;

import org.egualpam.services.hotel.rating.domain.Review;
import org.egualpam.services.hotel.rating.domain.ReviewRepository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.stream.Collectors;

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
                                org.egualpam.services.hotel.rating.infrastructure.persistance.jpa.Review.class)
                        // TODO: Receive 'hotelIdentifier' as Integer if it makes more sense
                        .setParameter("hotel_id", Integer.parseInt(hotelIdentifier));

        // TODO: Find a cleaner way to do this
        List<org.egualpam.services.hotel.rating.infrastructure.persistance.jpa.Review> results =
                (List<org.egualpam.services.hotel.rating.infrastructure.persistance.jpa.Review>)
                        query.getResultList();

        return results.stream()
                .map(review ->
                        mapIntoEntity(
                                review.getId().toString(),
                                review.getRating(),
                                review.getComment()))
                .collect(Collectors.toList());
    }
}