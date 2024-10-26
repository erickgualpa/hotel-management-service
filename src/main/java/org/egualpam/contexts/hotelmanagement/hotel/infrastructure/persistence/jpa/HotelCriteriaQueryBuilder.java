package org.egualpam.contexts.hotelmanagement.hotel.infrastructure.persistence.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.persistence.jpa.PersistenceHotel;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class HotelCriteriaQueryBuilder {

    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String LOCATION = "location";
    private static final String TOTAL_PRICE = "totalPrice";
    private static final String IMAGE_URL = "imageURL";

    private final CriteriaBuilder criteriaBuilder;
    private final CriteriaQuery<PersistenceHotel> criteriaQuery;
    private final Root<PersistenceHotel> root;
    private final List<Predicate> filters = new ArrayList<>();

    public HotelCriteriaQueryBuilder(EntityManager entityManager) {
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
        this.criteriaQuery = criteriaBuilder.createQuery(PersistenceHotel.class);
        this.root = criteriaQuery.from(PersistenceHotel.class);
        this.criteriaQuery.select(
                criteriaBuilder.construct(
                        PersistenceHotel.class,
                        root.get(ID),
                        root.get(NAME),
                        root.get(DESCRIPTION),
                        root.get(LOCATION),
                        root.get(TOTAL_PRICE),
                        root.get(IMAGE_URL)
                )
        );
    }

    public HotelCriteriaQueryBuilder withLocation(Optional<String> location) {
        location.ifPresent(
                targetLocation -> filters.add(
                        criteriaBuilder.equal(root.get(LOCATION), targetLocation)
                )
        );
        return this;
    }

    public HotelCriteriaQueryBuilder withMinPrice(Optional<Integer> minPrice) {
        minPrice.ifPresent(
                targetMinPrice -> filters.add(
                        criteriaBuilder.greaterThanOrEqualTo(
                                root.get(TOTAL_PRICE),
                                targetMinPrice
                        )
                )
        );
        return this;
    }

    public HotelCriteriaQueryBuilder withMaxPrice(Optional<Integer> maxPrice) {
        maxPrice.ifPresent(
                targetMaxPrice -> filters.add(
                        criteriaBuilder.lessThanOrEqualTo(
                                root.get(TOTAL_PRICE),
                                targetMaxPrice
                        )
                )
        );
        return this;
    }

    public CriteriaQuery<PersistenceHotel> build() {
        return criteriaQuery.where(filters.toArray(new Predicate[0]));
    }
}
