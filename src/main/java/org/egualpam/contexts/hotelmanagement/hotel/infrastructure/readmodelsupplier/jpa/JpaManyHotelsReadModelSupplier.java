package org.egualpam.contexts.hotelmanagement.hotel.infrastructure.readmodelsupplier.jpa;

import static java.util.Comparator.comparingDouble;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.UUID;
import org.egualpam.contexts.hotelmanagement.hotel.application.query.ManyHotels;
import org.egualpam.contexts.hotelmanagement.hotel.domain.HotelCriteria;
import org.egualpam.contexts.hotelmanagement.hotel.domain.Location;
import org.egualpam.contexts.hotelmanagement.hotel.domain.Price;
import org.egualpam.contexts.hotelmanagement.hotel.infrastructure.shared.jpa.PersistenceHotel;
import org.egualpam.contexts.hotelmanagement.shared.application.query.ReadModelSupplier;

public class JpaManyHotelsReadModelSupplier
    implements ReadModelSupplier<HotelCriteria, ManyHotels> {

  private final EntityManager entityManager;
  private final GetHotelAverageRating getHotelAverageRating;

  public JpaManyHotelsReadModelSupplier(EntityManager entityManager) {
    this.entityManager = entityManager;
    this.getHotelAverageRating = new GetHotelAverageRating(entityManager);
  }

  @Override
  public ManyHotels get(HotelCriteria criteria) {
    String location = criteria.getLocation().map(Location::value).orElse(null);
    Integer minPrice = criteria.getMinPrice().map(Price::value).orElse(null);
    Integer maxPrice = criteria.getMaxPrice().map(Price::value).orElse(null);

    CriteriaQuery<PersistenceHotel> criteriaQuery =
        new HotelCriteriaQueryBuilder(entityManager)
            .withLocation(location)
            .withMinPrice(minPrice)
            .withMaxPrice(maxPrice)
            .build();

    List<ManyHotels.Hotel> hotels =
        entityManager.createQuery(criteriaQuery).getResultList().stream()
            .map(this::mapIntoViewHotel)
            .sorted(comparingDouble(ManyHotels.Hotel::averageRating).reversed())
            .toList();

    return new ManyHotels(hotels);
  }

  private ManyHotels.Hotel mapIntoViewHotel(PersistenceHotel persistenceHotel) {
    UUID hotelId = persistenceHotel.getId();
    HotelAverageRating hotelAverageRating = getHotelAverageRating.using(hotelId);
    return new ManyHotels.Hotel(
        hotelId.toString(),
        persistenceHotel.getName(),
        persistenceHotel.getDescription(),
        persistenceHotel.getLocation(),
        persistenceHotel.getPrice(),
        persistenceHotel.getImageURL(),
        hotelAverageRating.value());
  }
}
