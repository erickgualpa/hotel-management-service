package org.egualpam.services.hotel.rating.infrastructure.persistence.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaQuery;
import org.egualpam.services.hotel.rating.application.hotels.HotelQuery;
import org.egualpam.services.hotel.rating.domain.hotels.Hotel;
import org.egualpam.services.hotel.rating.domain.hotels.HotelRepository;
import org.egualpam.services.hotel.rating.infrastructure.persistence.HotelDto;

import java.util.List;

public class PostgreSqlJpaHotelRepository extends HotelRepository {

    private final EntityManager entityManager;
    private final HotelCriteriaQueryBuilder hotelCriteriaQueryBuilder;

    public PostgreSqlJpaHotelRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.hotelCriteriaQueryBuilder = new HotelCriteriaQueryBuilder(entityManager);
    }

    @Override
    public List<Hotel> findHotelsMatchingQuery(HotelQuery hotelQuery) {

        CriteriaQuery<HotelDto> criteriaQuery = hotelCriteriaQueryBuilder.buildFrom(hotelQuery);

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