package org.egualpam.contexts.hotelmanagement.hotelrating.infrastructure.repository.jpa;

import static java.util.Objects.isNull;

import jakarta.persistence.EntityManager;
import java.util.Optional;
import java.util.UUID;
import org.egualpam.contexts.hotelmanagement.hotelrating.domain.HotelRating;
import org.egualpam.contexts.hotelmanagement.shared.domain.ActionNotYetImplemented;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateId;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateRepository;

public final class JpaHotelRatingRepository implements AggregateRepository<HotelRating> {

  private final EntityManager entityManager;

  public JpaHotelRatingRepository(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Override
  public Optional<HotelRating> find(AggregateId id) {
    UUID hotelRatingId = UUID.fromString(id.value());

    PersistenceHotelRating persistenceHotelRating =
        entityManager.find(PersistenceHotelRating.class, hotelRatingId);

    if (isNull(persistenceHotelRating)) {
      return Optional.empty();
    }

    throw new ActionNotYetImplemented();
  }

  @Override
  public void save(HotelRating hotelRating) {
    String query =
        """
        INSERT INTO hotel_rating(id, hotel_id, rating_sum, review_count, avg_value)
        VALUES(:id, :hotelId, :ratingSum, :reviewCount, :averageRating)
        ON CONFLICT (id)
        DO UPDATE SET
          rating_sum=:ratingSum,
          review_count=:reviewCount,
          avg_value=:averageRating
        """;

    entityManager
        .createNativeQuery(query)
        .setParameter("id", UUID.fromString(hotelRating.id().value()))
        .setParameter("hotelId", UUID.fromString(hotelRating.hotelId()))
        .setParameter("ratingSum", hotelRating.ratingSum())
        .setParameter("reviewCount", hotelRating.reviewsCount())
        .setParameter("averageRating", hotelRating.average())
        .executeUpdate();

    entityManager.flush();
  }
}
