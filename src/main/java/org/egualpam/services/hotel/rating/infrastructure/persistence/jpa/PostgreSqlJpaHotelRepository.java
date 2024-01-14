package org.egualpam.services.hotel.rating.infrastructure.persistence.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaQuery;
import org.egualpam.services.hotel.rating.domain.hotels.Hotel;
import org.egualpam.services.hotel.rating.domain.hotels.HotelRepository;
import org.egualpam.services.hotel.rating.domain.hotels.Location;
import org.egualpam.services.hotel.rating.domain.hotels.Price;
import org.egualpam.services.hotel.rating.domain.hotels.PriceRange;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public final class PostgreSqlJpaHotelRepository extends HotelRepository {

    private final EntityManager entityManager;
    private final Function<PersistenceHotel, List<PersistenceReview>> findReviewsByHotel;

    public PostgreSqlJpaHotelRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.findReviewsByHotel = new FindReviewsByHotel(entityManager);
    }

    @Override
    public List<Hotel> find(Optional<Location> location, PriceRange priceRange) {
        CriteriaQuery<PersistenceHotel> criteriaQuery =
                new HotelCriteriaQueryBuilder(entityManager)
                        .withLocation(location.map(Location::value))
                        .withMinPrice(priceRange.minPrice().map(Price::value))
                        .withMaxPrice(priceRange.maxPrice().map(Price::value))
                        .build();

        return entityManager
                .createQuery(criteriaQuery)
                .getResultList()
                .stream()
                .map(
                        persistenceHotel ->
                                mapIntoEntity(
                                        persistenceHotel.getId().toString(),
                                        persistenceHotel.getName(),
                                        persistenceHotel.getDescription(),
                                        persistenceHotel.getLocation(),
                                        persistenceHotel.getTotalPrice(),
                                        persistenceHotel.getImageURL(),
                                        findReviewsByHotel
                                                .apply(persistenceHotel)
                                                .stream()
                                                .mapToDouble(PersistenceReview::getRating)
                                                .filter(Objects::nonNull)
                                                .average()
                                                .orElse(0.0)
                                )
                )
                .toList();
    }
}