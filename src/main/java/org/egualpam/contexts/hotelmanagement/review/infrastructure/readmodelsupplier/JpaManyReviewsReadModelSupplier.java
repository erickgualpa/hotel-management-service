package org.egualpam.contexts.hotelmanagement.review.infrastructure.readmodelsupplier;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.util.List;
import java.util.UUID;
import org.egualpam.contexts.hotel.shared.application.query.ReadModelSupplier;
import org.egualpam.contexts.hotel.shared.domain.RequiredPropertyIsMissing;
import org.egualpam.contexts.hotelmanagement.review.application.query.ManyReviews;
import org.egualpam.contexts.hotelmanagement.review.domain.HotelId;
import org.egualpam.contexts.hotelmanagement.review.domain.ReviewCriteria;

public class JpaManyReviewsReadModelSupplier
    implements ReadModelSupplier<ReviewCriteria, ManyReviews> {

  private final EntityManager entityManager;

  public JpaManyReviewsReadModelSupplier(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Override
  public ManyReviews get(ReviewCriteria criteria) {
    HotelId hotelId = criteria.getHotelId().orElseThrow(RequiredPropertyIsMissing::new);

    String sql =
        """
                SELECT r.rating, r.comment
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
            .map(review -> new ManyReviews.Review(review.rating().intValue(), review.comment()))
            .toList();

    return new ManyReviews(reviews);
  }

  record PersistenceReview(Long rating, String comment) {}
}
