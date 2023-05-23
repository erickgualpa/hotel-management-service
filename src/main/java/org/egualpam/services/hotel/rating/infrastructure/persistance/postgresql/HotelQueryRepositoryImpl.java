package org.egualpam.services.hotel.rating.infrastructure.persistance.postgresql;

import static java.util.Objects.nonNull;

import java.util.ArrayList;
import java.util.List;
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
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Hotel> criteriaQuery = criteriaBuilder.createQuery(Hotel.class);

        Root<Hotel> hotel = criteriaQuery.from(Hotel.class);
        List<Predicate> filters = new ArrayList<>();
        if (nonNull(hotelQuery.getLocation())) {
            Predicate locationFilter =
                    criteriaBuilder.equal(hotel.get("location"), hotelQuery.getLocation());
            filters.add(locationFilter);
        }

        if (nonNull(hotelQuery.getPriceRange())) {
            Predicate minPriceFilter =
                    criteriaBuilder.greaterThan(
                            hotel.get("totalPrice"), hotelQuery.getPriceRange().getBegin());
            Predicate maxPriceFilter =
                    criteriaBuilder.lessThan(
                            hotel.get("totalPrice"), hotelQuery.getPriceRange().getEnd());

            filters.add(minPriceFilter);
            filters.add(maxPriceFilter);
        }

        criteriaQuery.where(filters.toArray(new Predicate[0]));

        TypedQuery<Hotel> query = entityManager.createQuery(criteriaQuery);

        return query.getResultList();
    }
}
