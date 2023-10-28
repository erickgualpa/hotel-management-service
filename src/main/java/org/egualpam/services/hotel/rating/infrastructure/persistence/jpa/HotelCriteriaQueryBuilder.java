package org.egualpam.services.hotel.rating.infrastructure.persistence.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.egualpam.services.hotel.rating.domain.hotels.Location;
import org.egualpam.services.hotel.rating.domain.hotels.Price;
import org.egualpam.services.hotel.rating.infrastructure.persistence.HotelDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HotelCriteriaQueryBuilder {

    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String LOCATION = "location";
    private static final String TOTAL_PRICE = "totalPrice";
    private static final String IMAGE_URL = "imageURL";

    private final CriteriaBuilder criteriaBuilder;

    public HotelCriteriaQueryBuilder(EntityManager entityManager) {
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
    }

    public CriteriaQuery<HotelDto> buildFrom(Optional<Location> location,
                                             Optional<Price> minPrice,
                                             Optional<Price> maxPrice) {
        CriteriaQuery<HotelDto> criteriaQuery = criteriaBuilder.createQuery(HotelDto.class);

        Root<Hotel> rootHotelEntity = criteriaQuery.from(Hotel.class);

        criteriaQuery.select(
                criteriaBuilder.construct(
                        HotelDto.class,
                        rootHotelEntity.get(ID),
                        rootHotelEntity.get(NAME),
                        rootHotelEntity.get(DESCRIPTION),
                        rootHotelEntity.get(LOCATION),
                        rootHotelEntity.get(TOTAL_PRICE),
                        rootHotelEntity.get(IMAGE_URL)));

        criteriaQuery.where(
                buildFilters(
                        rootHotelEntity,
                        location,
                        minPrice,
                        maxPrice)
        );

        return criteriaQuery;
    }

    private Predicate[] buildFilters(Root<Hotel> rootEntity,
                                     Optional<Location> location,
                                     Optional<Price> minPrice,
                                     Optional<Price> maxPrice
    ) {
        List<Predicate> filters = new ArrayList<>();

        location.ifPresent(
                targetLocation -> filters.add(
                        locationFilter(rootEntity, targetLocation.value())
                )
        );

        minPrice.ifPresent(
                targetMinPrice -> filters.add(
                        minPriceFilter(rootEntity, targetMinPrice.value())
                )
        );

        maxPrice.ifPresent(
                targetMaxPrice -> filters.add(
                        maxPriceFilter(rootEntity, targetMaxPrice.value())
                )
        );

        return filters.toArray(new Predicate[0]);
    }

    private Predicate locationFilter(Root<Hotel> rootEntity, String location) {
        return criteriaBuilder.equal(rootEntity.get(LOCATION), location);
    }

    private Predicate minPriceFilter(Root<Hotel> hotel, Integer minPrice) {
        return criteriaBuilder.greaterThanOrEqualTo(hotel.get(TOTAL_PRICE), minPrice);
    }

    private Predicate maxPriceFilter(Root<Hotel> hotel, Integer maxPrice) {
        return criteriaBuilder.lessThanOrEqualTo(hotel.get(TOTAL_PRICE), maxPrice);
    }
}
