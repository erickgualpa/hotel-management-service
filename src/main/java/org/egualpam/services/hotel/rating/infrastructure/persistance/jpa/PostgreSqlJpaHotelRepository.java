package org.egualpam.services.hotel.rating.infrastructure.persistance.jpa;

import org.egualpam.services.hotel.rating.application.HotelQuery;
import org.egualpam.services.hotel.rating.domain.RatedHotel;
import org.egualpam.services.hotel.rating.domain.RatedHotelRepository;
import org.egualpam.services.hotel.rating.infrastructure.persistance.HotelDto;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.stream.Collectors;

public class PostgreSqlJpaHotelRepository extends RatedHotelRepository {

    private final EntityManager entityManager;
    private final HotelCriteriaQueryBuilder hotelCriteriaQueryBuilder;

    public PostgreSqlJpaHotelRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.hotelCriteriaQueryBuilder = new HotelCriteriaQueryBuilder(entityManager);
    }

    @Override
    public List<RatedHotel> findHotelsMatchingQuery(HotelQuery hotelQuery) {

        CriteriaQuery<HotelDto> criteriaQuery = hotelCriteriaQueryBuilder.buildFrom(hotelQuery);

        return entityManager.createQuery(criteriaQuery)
                .getResultList()
                .stream()
                .map(
                        hotelDto ->
                                buildEntity(
                                        hotelDto.id().toString(),
                                        hotelDto.name(),
                                        hotelDto.description(),
                                        hotelDto.location(),
                                        hotelDto.totalPrice(),
                                        hotelDto.imageURL()
                                )
                )
                .collect(Collectors.toList());
    }
}
