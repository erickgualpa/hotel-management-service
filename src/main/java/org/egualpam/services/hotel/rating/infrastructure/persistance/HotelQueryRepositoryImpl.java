package org.egualpam.services.hotel.rating.infrastructure.persistance;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.egualpam.services.hotel.rating.application.HotelQuery;
import org.egualpam.services.hotel.rating.infrastructure.persistance.entity.Hotel;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class HotelQueryRepositoryImpl implements HotelQueryRepository {

    private final EntityManager entityManager;

    @Override
    public List<Hotel> findHotelsMatchingQuery(HotelQuery hotelQuery) {
        CriteriaQuery<Hotel> criteriaQuery = buildCriteriaQuery(hotelQuery);
        TypedQuery<Hotel> query = entityManager.createQuery(criteriaQuery);
        return query.getResultList();
    }

    @Override
    public void registerHotel(Hotel hotel) {
        entityManager.persist(hotel);
        entityManager.flush();
    }

    private CriteriaQuery<Hotel> buildCriteriaQuery(HotelQuery hotelQuery) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Hotel> criteriaQuery = criteriaBuilder.createQuery(Hotel.class);

        Root<Hotel> rootEntity = criteriaQuery.from(Hotel.class);
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
