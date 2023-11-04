package org.egualpam.services.hotel.rating.infrastructure.persistence.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import java.util.List;
import java.util.function.Function;

public final class FindReviewsByHotel implements Function<PersistenceHotel, List<PersistenceReview>> {

    private static final String findReviewsByHotelId = """
            SELECT r.id, r.rating, r.comment, r.hotel_id
            FROM reviews r
            WHERE r.hotel_id = :hotel_id
            """;

    private final Query query;

    public FindReviewsByHotel(EntityManager entityManager) {
        this.query = entityManager.createNativeQuery(
                findReviewsByHotelId,
                PersistenceHotel.class
        );
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<PersistenceReview> apply(PersistenceHotel hotel) {
        return query.setParameter("hotel_id", hotel.getId()).getResultList();
    }
}
