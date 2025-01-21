package org.egualpam.contexts.hotelmanagement.hotel.infrastructure.repository.jpa;

import static java.util.Map.entry;

import jakarta.persistence.EntityManager;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.egualpam.contexts.hotelmanagement.hotel.domain.Hotel;
import org.egualpam.contexts.hotelmanagement.hotel.infrastructure.shared.jpa.GetHotelAverageRating;
import org.egualpam.contexts.hotelmanagement.hotel.infrastructure.shared.jpa.HotelAverageRating;
import org.egualpam.contexts.hotelmanagement.hotel.infrastructure.shared.jpa.PersistenceHotel;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateId;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateRepository;

public final class JpaHotelRepository implements AggregateRepository<Hotel> {

  private final EntityManager entityManager;
  private final GetHotelAverageRating getHotelAverageRating;

  public JpaHotelRepository(EntityManager entityManager) {
    this.entityManager = entityManager;
    this.getHotelAverageRating = new GetHotelAverageRating(entityManager);
  }

  @Override
  public Optional<Hotel> find(AggregateId id) {
    UUID hotelId = UUID.fromString(id.value());
    PersistenceHotel persistenceHotel = entityManager.find(PersistenceHotel.class, hotelId);
    return Optional.ofNullable(persistenceHotel).map(this::mapResultIntoHotel);
  }

  private Hotel mapResultIntoHotel(PersistenceHotel persistenceHotel) {
    UUID hotelId = persistenceHotel.getId();
    String name = persistenceHotel.getName();
    String description = persistenceHotel.getDescription();
    String location = persistenceHotel.getLocation();
    Integer price = persistenceHotel.getPrice();
    String imageURL = persistenceHotel.getImageURL();

    HotelAverageRating hotelAverageRating = getHotelAverageRating.using(hotelId);

    return Hotel.load(
        Map.ofEntries(
            entry("id", hotelId.toString()),
            entry("name", name),
            entry("description", description),
            entry("location", location),
            entry("price", price),
            entry("imageURL", imageURL),
            entry("ratingReviewsCount", hotelAverageRating.reviewsCount()),
            entry("ratingAverage", hotelAverageRating.value())));
  }

  @Override
  public void save(Hotel hotel) {
    PersistenceHotel persistenceHotel = new PersistenceHotel();
    UUID hotelId = UUID.fromString(hotel.id().value());
    persistenceHotel.setId(hotelId);
    persistenceHotel.setName(hotel.name().value());
    persistenceHotel.setDescription(hotel.description().value());
    persistenceHotel.setLocation(hotel.location().value());
    persistenceHotel.setPrice(hotel.price().value());
    persistenceHotel.setImageURL(hotel.imageURL().value());

    entityManager.merge(persistenceHotel);
    updateHotelAverageRating(hotel);

    entityManager.flush();
  }

  private void updateHotelAverageRating(Hotel hotel) {
    String query =
        """
        INSERT INTO hotel_average_rating(hotel_id, rating_sum, review_count, avg_value)
        VALUES(:hotelId, :ratingSum, :reviewCount, :averageRating)
        ON CONFLICT (hotel_id)
        DO UPDATE SET
          rating_sum=:ratingSum,
          review_count=:reviewCount,
          avg_value=:averageRating
        """;

    entityManager
        .createNativeQuery(query)
        .setParameter("hotelId", UUID.fromString(hotel.id().value()))
        .setParameter("ratingSum", hotel.rating().ratingSum())
        .setParameter("reviewCount", hotel.rating().reviewsCount())
        .setParameter("averageRating", hotel.rating().average())
        .executeUpdate();
  }
}
