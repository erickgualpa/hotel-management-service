package org.egualpam.services.hotel.rating.infrastructure.persistance.jpa;

import org.egualpam.services.hotel.rating.application.HotelQuery;
import org.egualpam.services.hotel.rating.domain.Hotel;
import org.egualpam.services.hotel.rating.domain.HotelRepository;
import org.egualpam.services.hotel.rating.infrastructure.persistance.HotelDto;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.stream.Collectors;

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
                                        // TODO: Identifier mapped should not be the db identifier but the entity one
                                        hotelDto.id().toString(),
                                        hotelDto.name(),
                                        hotelDto.description(),
                                        hotelDto.location(),
                                        hotelDto.totalPrice(),
                                        hotelDto.imageURL())
                )
                .collect(Collectors.toList());
    }
}