package org.egualpam.services.hotel.rating.infrastructure.persistance.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.egualpam.services.hotel.rating.application.HotelQuery;
import org.egualpam.services.hotel.rating.infrastructure.persistance.HotelDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HotelCriteriaQueryBuilder {

    private final CriteriaBuilder criteriaBuilder;

    public HotelCriteriaQueryBuilder(EntityManager entityManager) {
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
    }

    public CriteriaQuery<HotelDto> buildFrom(HotelQuery hotelQuery) {
        CriteriaQuery<HotelDto> criteriaQuery = criteriaBuilder.createQuery(HotelDto.class);

        Root<Hotel> rootHotelEntity = criteriaQuery.from(Hotel.class);

        criteriaQuery.select(
                criteriaBuilder.construct(
                        HotelDto.class,
                        rootHotelEntity.get("id"),
                        rootHotelEntity.get("name"),
                        rootHotelEntity.get("description"),
                        rootHotelEntity.get("location"),
                        rootHotelEntity.get("totalPrice"),
                        rootHotelEntity.get("imageURL")));

        criteriaQuery.where(buildFilters(hotelQuery, rootHotelEntity));

        return criteriaQuery;
    }

    private Predicate[] buildFilters(HotelQuery hotelQuery, Root<Hotel> rootEntity) {
        List<Predicate> filters = new ArrayList<>();

        Optional.ofNullable(hotelQuery.getLocation())
                .ifPresent(
                        location -> filters.add(locationFilter(rootEntity, location)));

        HotelQuery.PriceRange targetPriceRange = hotelQuery.getPriceRange();

        Optional.ofNullable(targetPriceRange)
                .map(HotelQuery.PriceRange::begin)
                .ifPresent(
                        minPrice -> filters.add(minPriceFilter(rootEntity, minPrice)));

        Optional.ofNullable(targetPriceRange)
                .map(HotelQuery.PriceRange::end)
                .ifPresent(
                        maxPrice -> filters.add(maxPriceFilter(rootEntity, maxPrice)));

        return filters.toArray(new Predicate[0]);
    }

    private Predicate locationFilter(Root<Hotel> rootEntity, String location) {
        return criteriaBuilder.equal(rootEntity.get("location"), location);
    }

    private Predicate minPriceFilter(Root<Hotel> hotel, Integer minPrice) {
        return criteriaBuilder.greaterThanOrEqualTo(hotel.get("totalPrice"), minPrice);
    }

    private Predicate maxPriceFilter(Root<Hotel> hotel, Integer maxPrice) {
        return criteriaBuilder.lessThanOrEqualTo(hotel.get("totalPrice"), maxPrice);
    }
}
