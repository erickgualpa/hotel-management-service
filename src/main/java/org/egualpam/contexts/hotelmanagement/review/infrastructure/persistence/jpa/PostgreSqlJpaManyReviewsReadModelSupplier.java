package org.egualpam.contexts.hotelmanagement.review.infrastructure.persistence.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.util.List;
import java.util.UUID;
import org.egualpam.contexts.hotelmanagement.review.application.query.ManyReviews;
import org.egualpam.contexts.hotelmanagement.review.domain.HotelId;
import org.egualpam.contexts.hotelmanagement.review.domain.ReviewCriteria;
import org.egualpam.contexts.hotelmanagement.shared.application.query.ReadModelSupplier;
import org.egualpam.contexts.hotelmanagement.shared.domain.Criteria;
import org.egualpam.contexts.hotelmanagement.shared.domain.exceptions.RequiredPropertyIsMissing;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.persistence.jpa.PersistenceReview;

public class PostgreSqlJpaManyReviewsReadModelSupplier implements ReadModelSupplier<ManyReviews> {

  private final EntityManager entityManager;

  public PostgreSqlJpaManyReviewsReadModelSupplier(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Override
  public ManyReviews get(Criteria criteria) {
    ReviewCriteria reviewCriteria = (ReviewCriteria) criteria;
    HotelId hotelId = reviewCriteria.getHotelId().orElseThrow(RequiredPropertyIsMissing::new);

    String sql =
        """
                SELECT r.id, r.rating, r.comment, r.hotel_id
                FROM reviews r
                WHERE r.hotel_id = :hotel_id
                """;

    Query query =
        entityManager
            .createNativeQuery(sql, PersistenceReview.class)
            .setParameter("hotel_id", UUID.fromString(hotelId.value()));

    List<PersistenceReview> persistenceReviews = query.getResultList();

    List<ManyReviews.Review> reviews =
        persistenceReviews.stream()
            .map(review -> new ManyReviews.Review(review.getRating(), review.getComment()))
            .toList();

    return new ManyReviews(reviews);
  }
}
