package org.egualpam.services.hotel.rating.infrastructure.persistence.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.egualpam.services.hotel.rating.domain.reviews.Comment;
import org.egualpam.services.hotel.rating.domain.reviews.Rating;
import org.egualpam.services.hotel.rating.domain.reviews.Review;
import org.egualpam.services.hotel.rating.domain.reviews.ReviewCriteria;
import org.egualpam.services.hotel.rating.domain.shared.AggregateId;
import org.egualpam.services.hotel.rating.domain.shared.AggregateRepository;
import org.egualpam.services.hotel.rating.domain.shared.Criteria;
import org.egualpam.services.hotel.rating.domain.shared.Identifier;

import java.util.List;
import java.util.UUID;

public class PostgreSqlJpaReviewRepository implements AggregateRepository<Review> {

    private final EntityManager entityManager;

    public PostgreSqlJpaReviewRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Review find(AggregateId id) {
        String sql = """
                SELECT r.id, r.rating, r.comment, r.hotel_id
                FROM reviews r
                WHERE r.id = :id
                """;

        Query query =
                entityManager
                        .createNativeQuery(sql, PersistenceReview.class)
                        .setParameter("id", id.value());

        PersistenceReview review = (PersistenceReview) query.getSingleResult();

        return new Review(
                new AggregateId(review.getId()),
                new Identifier(review.getHotelId().toString()),
                new Rating(review.getRating()),
                new Comment(review.getComment())
        );
    }

    @Override
    public List<Review> find(Criteria reviewCriteria) {
        UUID hotelId = UUID.fromString(((ReviewCriteria) reviewCriteria).getHotelId().value());

        String sql = """
                SELECT r.id, r.rating, r.comment, r.hotel_id
                FROM reviews r
                WHERE r.hotel_id = :hotel_id
                """;

        Query query =
                entityManager
                        .createNativeQuery(sql, PersistenceReview.class)
                        .setParameter("hotel_id", hotelId);

        List<PersistenceReview> reviews = query.getResultList();

        return reviews.stream()
                .map(
                        review ->
                                new Review(
                                        new AggregateId(review.getId()),
                                        new Identifier(review.getHotelId().toString()),
                                        new Rating(review.getRating()),
                                        new Comment(review.getComment())))
                .toList();
    }

    @Override
    @Transactional
    public void save(Review review) {
        PersistenceReview persistenceReview = new PersistenceReview();
        persistenceReview.setId(review.getId().value());
        persistenceReview.setHotelId(UUID.fromString(review.getHotelIdentifier().value()));
        persistenceReview.setRating(review.getRating().value());
        persistenceReview.setComment(review.getComment().value());

        entityManager.merge(persistenceReview);
        entityManager.flush();
    }
}