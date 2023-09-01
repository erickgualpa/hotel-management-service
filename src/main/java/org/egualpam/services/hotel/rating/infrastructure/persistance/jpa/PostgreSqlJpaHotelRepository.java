package org.egualpam.services.hotel.rating.infrastructure.persistance.jpa;

import org.egualpam.services.hotel.rating.application.HotelQuery;
import org.egualpam.services.hotel.rating.domain.Location;
import org.egualpam.services.hotel.rating.domain.RatedHotel;
import org.egualpam.services.hotel.rating.domain.RatedHotelRepository;
import org.egualpam.services.hotel.rating.infrastructure.persistance.HotelDto;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class PostgreSqlJpaHotelRepository implements RatedHotelRepository {

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
                .map(this::mapToEntity)
                .collect(Collectors.toList());
    }

    private RatedHotel mapToEntity(HotelDto hotelDto) {

        return new RatedHotel(
                hotelDto.id().toString(),
                hotelDto.name(),
                hotelDto.description(),
                // TODO: Decide what how 'Location' will be managed (Entity or Value)
                new Location(UUID.randomUUID().toString(), hotelDto.location()),
                hotelDto.totalPrice(),
                hotelDto.imageURL()
        );
    }
}
