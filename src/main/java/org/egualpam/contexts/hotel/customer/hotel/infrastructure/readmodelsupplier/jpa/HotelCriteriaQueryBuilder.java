package org.egualpam.contexts.hotel.customer.hotel.infrastructure.readmodelsupplier.jpa;

import static java.util.Objects.nonNull;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import org.egualpam.contexts.hotel.shared.infrastructure.jpa.PersistenceHotel;

final class HotelCriteriaQueryBuilder {

  private static final String ID = "id";
  private static final String NAME = "name";
  private static final String DESCRIPTION = "description";
  private static final String LOCATION = "location";
  private static final String PRICE = "price";
  private static final String IMAGE_URL = "imageURL";

  private final CriteriaBuilder criteriaBuilder;
  private final CriteriaQuery<PersistenceHotel> criteriaQuery;
  private final Root<PersistenceHotel> root;
  private final List<Predicate> filters = new ArrayList<>();

  HotelCriteriaQueryBuilder(EntityManager entityManager) {
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
            root.get(PRICE),
            root.get(IMAGE_URL)));
  }

  HotelCriteriaQueryBuilder withLocation(String location) {
    if (nonNull(location)) {
      filters.add(criteriaBuilder.equal(root.get(LOCATION), location));
    }
    return this;
  }

  HotelCriteriaQueryBuilder withMinPrice(Integer minPrice) {
    if (nonNull(minPrice)) {
      filters.add(criteriaBuilder.greaterThanOrEqualTo(root.get(PRICE), minPrice));
    }
    return this;
  }

  HotelCriteriaQueryBuilder withMaxPrice(Integer maxPrice) {
    if (nonNull(maxPrice)) {
      filters.add(criteriaBuilder.lessThanOrEqualTo(root.get(PRICE), maxPrice));
    }
    return this;
  }

  CriteriaQuery<PersistenceHotel> build() {
    return criteriaQuery.where(filters.toArray(new Predicate[0]));
  }
}
