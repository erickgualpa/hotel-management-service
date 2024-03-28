package org.egualpam.services.hotelmanagement.hotels.infrastructure.persistence.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaQuery;
import org.egualpam.services.hotelmanagement.hotels.application.MultipleHotelsView;
import org.egualpam.services.hotelmanagement.hotels.domain.HotelCriteria;
import org.egualpam.services.hotelmanagement.hotels.domain.Location;
import org.egualpam.services.hotelmanagement.hotels.domain.Price;
import org.egualpam.services.hotelmanagement.hotels.domain.PriceRange;
import org.egualpam.services.hotelmanagement.shared.application.ViewSupplier;
import org.egualpam.services.hotelmanagement.shared.domain.Criteria;
import org.egualpam.services.hotelmanagement.shared.infrastructure.persistence.jpa.PersistenceHotel;
import org.egualpam.services.hotelmanagement.shared.infrastructure.persistence.jpa.PersistenceReview;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import static java.util.Comparator.comparingDouble;

public class PostgreSqlJpaMultipleHotelsViewSupplier implements ViewSupplier<MultipleHotelsView> {

    private final EntityManager entityManager;
    private final Function<PersistenceHotel, List<PersistenceReview>> findReviewsByHotel;

    public PostgreSqlJpaMultipleHotelsViewSupplier(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.findReviewsByHotel = new FindReviewsByHotel(entityManager);
    }

    @Override
    public MultipleHotelsView get(Criteria criteria) {
        PriceRange priceRange = ((HotelCriteria) criteria).getPriceRange();
        Optional<String> location = ((HotelCriteria) criteria).getLocation().map(Location::value);

        CriteriaQuery<PersistenceHotel> criteriaQuery =
                new HotelCriteriaQueryBuilder(entityManager)
                        .withLocation(location)
                        .withMinPrice(priceRange.minPrice().map(Price::value))
                        .withMaxPrice(priceRange.maxPrice().map(Price::value))
                        .build();

        List<MultipleHotelsView.Hotel> hotels = entityManager
                .createQuery(criteriaQuery)
                .getResultList()
                .stream()
                .map(this::mapIntoViewHotel)
                .sorted(comparingDouble(MultipleHotelsView.Hotel::averageRating).reversed())
                .toList();

        return new MultipleHotelsView(hotels);
    }

    private MultipleHotelsView.Hotel mapIntoViewHotel(PersistenceHotel persistenceHotel) {
        double averageRating = findReviewsByHotel
                .apply(persistenceHotel)
                .stream()
                .mapToDouble(PersistenceReview::getRating)
                .filter(Objects::nonNull)
                .average()
                .orElse(0.0);
        return new MultipleHotelsView.Hotel(
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
