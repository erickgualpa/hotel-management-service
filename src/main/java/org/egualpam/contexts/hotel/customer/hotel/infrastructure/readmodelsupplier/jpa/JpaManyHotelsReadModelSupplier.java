package org.egualpam.contexts.hotel.customer.hotel.infrastructure.readmodelsupplier.jpa;

import static java.util.Comparator.comparingDouble;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.UUID;
import org.egualpam.contexts.hotel.customer.hotel.application.query.ManyHotels;
import org.egualpam.contexts.hotel.customer.hotel.domain.HotelCriteria;
import org.egualpam.contexts.hotel.customer.hotel.infrastructure.shared.jpa.hotelaveragerating.GetHotelAverageRating;
import org.egualpam.contexts.hotel.customer.hotel.infrastructure.shared.jpa.hotelaveragerating.HotelAverageRating;
import org.egualpam.contexts.hotel.shared.application.query.ReadModelSupplier;
import org.egualpam.contexts.hotel.shared.infrastructure.jpa.PersistenceHotel;

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
    String location = criteria.getLocation().orElse(null);
    Integer minPrice = criteria.getMinPrice().orElse(null);
    Integer maxPrice = criteria.getMaxPrice().orElse(null);

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
