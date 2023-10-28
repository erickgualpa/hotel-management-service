package org.egualpam.services.hotel.rating.infrastructure.persistence.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaQuery;
import org.egualpam.services.hotel.rating.domain.hotels.Hotel;
import org.egualpam.services.hotel.rating.domain.hotels.HotelRepository;
import org.egualpam.services.hotel.rating.domain.hotels.Location;
import org.egualpam.services.hotel.rating.domain.hotels.Price;
import org.egualpam.services.hotel.rating.infrastructure.persistence.HotelDto;

import java.util.List;
import java.util.Optional;

public class PostgreSqlJpaHotelRepository extends HotelRepository {

    private final EntityManager entityManager;
    private final HotelCriteriaQueryBuilder hotelCriteriaQueryBuilder;

    public PostgreSqlJpaHotelRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.hotelCriteriaQueryBuilder = new HotelCriteriaQueryBuilder(entityManager);
    }

    @Override
    public List<Hotel> findHotels(Optional<Location> location,
                                  Optional<Price> minPrice,
                                  Optional<Price> maxPrice) {
        CriteriaQuery<HotelDto> criteriaQuery = hotelCriteriaQueryBuilder.buildFrom(location, minPrice, maxPrice);

        return entityManager.createQuery(criteriaQuery)
                .getResultList()
                .stream()
                .map(
                        hotelDto ->
                                mapIntoEntity(
                                        hotelDto.id().toString(),
                                        hotelDto.name(),
                                        hotelDto.description(),
                                        hotelDto.location(),
                                        hotelDto.totalPrice(),
                                        hotelDto.imageURL()
                                )
                ).toList();
    }

}