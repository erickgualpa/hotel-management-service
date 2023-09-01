package org.egualpam.services.hotel.rating.infrastructure.persistance.jpa;

import org.egualpam.services.hotel.rating.application.HotelQuery;
import org.egualpam.services.hotel.rating.infrastructure.persistance.dto.HotelDto;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HotelCriteriaQueryBuilder {

    private final EntityManager entityManager;

    public HotelCriteriaQueryBuilder(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<HotelDto> findHotelsBy(HotelQuery hotelQuery) {
        CriteriaQuery<HotelDto> criteriaQuery = buildCriteriaQuery(hotelQuery);
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    private CriteriaQuery<HotelDto> buildCriteriaQuery(HotelQuery hotelQuery) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<HotelDto> criteriaQuery = criteriaBuilder.createQuery(HotelDto.class);

        Root<Hotel> rootEntity = criteriaQuery.from(Hotel.class);

        criteriaQuery.select(
                criteriaBuilder.construct(
                        HotelDto.class,
                        rootEntity.get("id"),
                        rootEntity.get("name"),
                        rootEntity.get("description"),
                        rootEntity.get("location"),
                        rootEntity.get("totalPrice"),
                        rootEntity.get("imageURL")
                )
        );

        List<Predicate> filters = new ArrayList<>();
        addLocationFilter(
                filters,
                Optional.ofNullable(hotelQuery.getLocation()),
                criteriaBuilder,
                rootEntity);
        addPriceRangeFilter(
                filters,
                Optional.ofNullable(hotelQuery.getPriceRange()),
                criteriaBuilder,
                rootEntity);

        criteriaQuery.where(filters.toArray(new Predicate[0]));
        return criteriaQuery;
    }

    private void addLocationFilter(
            List<Predicate> filters,
            Optional<String> targetLocation,
            CriteriaBuilder criteriaBuilder,
            Root<Hotel> rootEntity) {
        targetLocation.ifPresent(
                tl -> filters.add(criteriaBuilder.equal(rootEntity.get("location"), tl)));
    }

    private void addPriceRangeFilter(
            List<Predicate> filters,
            Optional<HotelQuery.PriceRange> targetPriceRange,
            CriteriaBuilder criteriaBuilder,
            Root<Hotel> hotel) {
        targetPriceRange.ifPresent(
                pr -> {
                    Predicate minPriceFilter =
                            criteriaBuilder.greaterThan(hotel.get("totalPrice"), pr.getBegin());
                    Predicate maxPriceFilter =
                            criteriaBuilder.lessThan(hotel.get("totalPrice"), pr.getEnd());

                    filters.add(minPriceFilter);
                    filters.add(maxPriceFilter);
                });
    }
}
