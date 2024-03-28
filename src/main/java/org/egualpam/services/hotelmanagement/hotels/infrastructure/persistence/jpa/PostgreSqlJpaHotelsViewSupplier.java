package org.egualpam.services.hotelmanagement.hotels.infrastructure.persistence.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaQuery;
import org.egualpam.services.hotelmanagement.application.hotels.HotelsView;
import org.egualpam.services.hotelmanagement.domain.hotels.HotelCriteria;
import org.egualpam.services.hotelmanagement.domain.hotels.Location;
import org.egualpam.services.hotelmanagement.domain.hotels.Price;
import org.egualpam.services.hotelmanagement.domain.hotels.PriceRange;
import org.egualpam.services.hotelmanagement.domain.shared.Criteria;
import org.egualpam.services.hotelmanagement.shared.application.ViewSupplier;
import org.egualpam.services.hotelmanagement.shared.infrastructure.persistence.jpa.PersistenceHotel;
import org.egualpam.services.hotelmanagement.shared.infrastructure.persistence.jpa.PersistenceReview;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import static java.util.Comparator.comparingDouble;

public class PostgreSqlJpaHotelsViewSupplier implements ViewSupplier<HotelsView> {

    private final EntityManager entityManager;
    private final Function<PersistenceHotel, List<PersistenceReview>> findReviewsByHotel;

    public PostgreSqlJpaHotelsViewSupplier(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.findReviewsByHotel = new FindReviewsByHotel(entityManager);
    }

    @Override
    public HotelsView get(Criteria criteria) {
        PriceRange priceRange = ((HotelCriteria) criteria).getPriceRange();
        Optional<String> location = ((HotelCriteria) criteria).getLocation().map(Location::value);

        CriteriaQuery<PersistenceHotel> criteriaQuery =
                new HotelCriteriaQueryBuilder(entityManager)
                        .withLocation(location)
                        .withMinPrice(priceRange.minPrice().map(Price::value))
                        .withMaxPrice(priceRange.maxPrice().map(Price::value))
                        .build();

        List<HotelsView.Hotel> hotels = entityManager
                .createQuery(criteriaQuery)
                .getResultList()
                .stream()
                .map(this::mapIntoViewHotel)
                .sorted(comparingDouble(HotelsView.Hotel::averageRating).reversed())
                .toList();

        return new HotelsView(hotels);
    }

    private HotelsView.Hotel mapIntoViewHotel(PersistenceHotel persistenceHotel) {
        double averageRating = findReviewsByHotel
                .apply(persistenceHotel)
                .stream()
                .mapToDouble(PersistenceReview::getRating)
                .filter(Objects::nonNull)
                .average()
                .orElse(0.0);
        return new HotelsView.Hotel(
                persistenceHotel.getId().toString(),
                persistenceHotel.getName(),
                persistenceHotel.getDescription(),
                persistenceHotel.getLocation(),
                persistenceHotel.getTotalPrice(),
                persistenceHotel.getImageURL(),
                averageRating
        );
    }
}
