package org.egualpam.contexts.hotel.customer.review.infrastructure.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import java.util.Optional;
import java.util.UUID;
import org.egualpam.contexts.hotel.customer.review.domain.Review;
import org.egualpam.contexts.hotel.shared.domain.AggregateId;
import org.egualpam.contexts.hotel.shared.domain.AggregateRepository;

public class JpaReviewRepository implements AggregateRepository<Review> {

  private final EntityManager entityManager;

  public JpaReviewRepository(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Override
  public Optional<Review> find(AggregateId id) {
    String sql =
        """
                SELECT r.id, r.rating, r.comment, r.hotel_id
                FROM reviews r
                WHERE r.id = :id
                """;

    Query query =
        entityManager
            .createNativeQuery(sql, ReadPersistenceReview.class)
            .setParameter("id", UUID.fromString(id.value()));

    final ReadPersistenceReview persistenceReview;
    try {
      persistenceReview = (ReadPersistenceReview) query.getSingleResult();
    } catch (NoResultException e) {
      return Optional.empty();
    }

    Review review =
        new Review(
            persistenceReview.id().toString(),
            persistenceReview.hotelId().toString(),
            persistenceReview.rating().intValue(),
            persistenceReview.comment());

    return Optional.of(review);
  }

  @Override
  public void save(Review review) {
    PersistenceReview persistenceReview = new PersistenceReview();
    persistenceReview.setId(UUID.fromString(review.id().value()));
    persistenceReview.setHotelId(UUID.fromString(review.hotelId().value()));
    persistenceReview.setRating(review.rating().value());
    persistenceReview.setComment(review.comment().value());

    entityManager.merge(persistenceReview);
    entityManager.flush();
  }

  record ReadPersistenceReview(UUID id, Long rating, String comment, UUID hotelId) {}
}
