package org.egualpam.contexts.hotelmanagement.hotel.infrastructure.readmodelsupplier.jpa;

import static java.util.Comparator.comparingDouble;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import org.egualpam.contexts.hotelmanagement.hotel.application.query.ManyHotels;
import org.egualpam.contexts.hotelmanagement.hotel.domain.HotelCriteria;
import org.egualpam.contexts.hotelmanagement.hotel.domain.Location;
import org.egualpam.contexts.hotelmanagement.hotel.domain.Price;
import org.egualpam.contexts.hotelmanagement.shared.application.query.ReadModelSupplier;
import org.egualpam.contexts.hotelmanagement.shared.domain.Criteria;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.persistence.jpa.PersistenceHotel;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.persistence.jpa.PersistenceReview;

public class JpaManyHotelsReadModelSupplier implements ReadModelSupplier<ManyHotels> {

  private final EntityManager entityManager;
  private final Function<PersistenceHotel, List<PersistenceReview>> findReviewsByHotel;

  public JpaManyHotelsReadModelSupplier(EntityManager entityManager) {
    this.entityManager = entityManager;
    this.findReviewsByHotel = new FindReviewsByHotel(entityManager);
  }

  @Override
  public ManyHotels get(Criteria criteria) {
    HotelCriteria hotelCriteria = (HotelCriteria) criteria;

    CriteriaQuery<PersistenceHotel> criteriaQuery =
        new HotelCriteriaQueryBuilder(entityManager)
            .withLocation(hotelCriteria.getLocation().map(Location::value))
            .withMinPrice(hotelCriteria.getMinPrice().map(Price::value))
            .withMaxPrice(hotelCriteria.getMaxPrice().map(Price::value))
            .build();

    List<ManyHotels.Hotel> hotels =
        entityManager.createQuery(criteriaQuery).getResultList().stream()
            .map(this::mapIntoViewHotel)
            .sorted(comparingDouble(ManyHotels.Hotel::averageRating).reversed())
            .toList();

    return new ManyHotels(hotels);
  }

  private ManyHotels.Hotel mapIntoViewHotel(PersistenceHotel persistenceHotel) {
    double averageRating =
        findReviewsByHotel.apply(persistenceHotel).stream()
            .mapToDouble(PersistenceReview::getRating)
            .filter(Objects::nonNull)
            .average()
            .orElse(0.0);
    return new ManyHotels.Hotel(
        persistenceHotel.getId().toString(),
        persistenceHotel.getName(),
        persistenceHotel.getDescription(),
        persistenceHotel.getLocation(),
        persistenceHotel.getPrice(),
        persistenceHotel.getImageURL(),
        averageRating);
  }
}
