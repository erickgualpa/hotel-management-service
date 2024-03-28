package org.egualpam.services.hotelmanagement.hotels.infrastructure.persistence.jpa;

import jakarta.persistence.EntityManager;
import org.egualpam.services.hotelmanagement.shared.infrastructure.persistence.jpa.PersistenceHotel;
import org.egualpam.services.hotelmanagement.shared.infrastructure.persistence.jpa.PersistenceReview;

import java.util.List;
import java.util.function.Function;

public final class FindReviewsByHotel implements Function<PersistenceHotel, List<PersistenceReview>> {

    private static final String findReviewsByHotelId = """
            SELECT r.id, r.rating, r.comment, r.hotel_id
            FROM reviews r
            WHERE r.hotel_id = :hotel_id
            """;

    private final EntityManager entityManager;

    public FindReviewsByHotel(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<PersistenceReview> apply(PersistenceHotel hotel) {
        return entityManager.createNativeQuery(
                        findReviewsByHotelId,
                        PersistenceReview.class
                )
                .setParameter("hotel_id", hotel.getId())
                .getResultList();
    }
}
