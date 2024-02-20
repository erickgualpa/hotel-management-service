package org.egualpam.services.hotel.rating.infrastructure.persistence.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaQuery;
import org.egualpam.services.hotel.rating.domain.hotels.AverageRating;
import org.egualpam.services.hotel.rating.domain.hotels.Hotel;
import org.egualpam.services.hotel.rating.domain.hotels.HotelCriteria;
import org.egualpam.services.hotel.rating.domain.hotels.HotelDescription;
import org.egualpam.services.hotel.rating.domain.hotels.HotelName;
import org.egualpam.services.hotel.rating.domain.hotels.ImageURL;
import org.egualpam.services.hotel.rating.domain.hotels.Location;
import org.egualpam.services.hotel.rating.domain.hotels.Price;
import org.egualpam.services.hotel.rating.domain.hotels.PriceRange;
import org.egualpam.services.hotel.rating.domain.shared.AggregateId;
import org.egualpam.services.hotel.rating.domain.shared.AggregateRepository;
import org.egualpam.services.hotel.rating.domain.shared.Criteria;
import org.egualpam.services.hotel.rating.domain.shared.Identifier;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public final class PostgreSqlJpaHotelRepository implements AggregateRepository<Hotel> {

    private final EntityManager entityManager;
    private final Function<PersistenceHotel, List<PersistenceReview>> findReviewsByHotel;

    public PostgreSqlJpaHotelRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.findReviewsByHotel = new FindReviewsByHotel(entityManager);
    }

    @Override
    public Hotel find(AggregateId id) {
        throw new RuntimeException("NOT_IMPLEMENTED");
    }

    @Override
    public List<Hotel> find(Criteria criteria) {
        PriceRange priceRange = ((HotelCriteria) criteria).getPriceRange();
        Optional<String> location = ((HotelCriteria) criteria).getLocation().map(Location::value);

        CriteriaQuery<PersistenceHotel> criteriaQuery =
                new HotelCriteriaQueryBuilder(entityManager)
                        .withLocation(location)
                        .withMinPrice(priceRange.minPrice().map(Price::value))
                        .withMaxPrice(priceRange.maxPrice().map(Price::value))
                        .build();

        return entityManager
                .createQuery(criteriaQuery)
                .getResultList()
                .stream()
                .map(this::mapResultIntoHotel)
                .toList();
    }

    private Hotel mapResultIntoHotel(PersistenceHotel persistenceHotel) {
        String name = persistenceHotel.getName();
        String description = persistenceHotel.getDescription();
        String location = persistenceHotel.getLocation();
        Integer totalPrice = persistenceHotel.getTotalPrice();
        String imageURL = persistenceHotel.getImageURL();
        AverageRating averageRating = new AverageRating(
                findReviewsByHotel
                        .apply(persistenceHotel)
                        .stream()
                        .mapToDouble(PersistenceReview::getRating)
                        .filter(Objects::nonNull)
                        .average()
                        .orElse(0.0)
        );
        return new Hotel(
                new Identifier(persistenceHotel.getId().toString()),
                new HotelName(name),
                new HotelDescription(description),
                new Location(location),
                new Price(totalPrice),
                new ImageURL(imageURL),
                averageRating
        );
    }

    @Override
    public void save(Hotel aggregate) {
        throw new RuntimeException("NOT_IMPLEMENTED");
    }
}