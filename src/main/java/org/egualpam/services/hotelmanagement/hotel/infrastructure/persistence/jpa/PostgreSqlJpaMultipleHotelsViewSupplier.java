package org.egualpam.services.hotelmanagement.hotel.infrastructure.persistence.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaQuery;
import org.egualpam.services.hotelmanagement.hotel.application.query.MultipleHotelsView;
import org.egualpam.services.hotelmanagement.hotel.domain.HotelCriteria;
import org.egualpam.services.hotelmanagement.hotel.domain.Location;
import org.egualpam.services.hotelmanagement.hotel.domain.Price;
import org.egualpam.services.hotelmanagement.shared.application.query.ViewSupplier;
import org.egualpam.services.hotelmanagement.shared.domain.Criteria;
import org.egualpam.services.hotelmanagement.shared.infrastructure.persistence.jpa.PersistenceHotel;
import org.egualpam.services.hotelmanagement.shared.infrastructure.persistence.jpa.PersistenceReview;

import java.util.List;
import java.util.Objects;
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
        HotelCriteria hotelCriteria = (HotelCriteria) criteria;

        CriteriaQuery<PersistenceHotel> criteriaQuery =
                new HotelCriteriaQueryBuilder(entityManager)
                        .withLocation(hotelCriteria.getLocation().map(Location::value))
                        .withMinPrice(hotelCriteria.getMinPrice().map(Price::value))
                        .withMaxPrice(hotelCriteria.getMaxPrice().map(Price::value))
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
