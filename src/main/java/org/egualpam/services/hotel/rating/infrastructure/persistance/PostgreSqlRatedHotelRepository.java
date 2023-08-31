package org.egualpam.services.hotel.rating.infrastructure.persistance;

import org.egualpam.services.hotel.rating.application.HotelQuery;
import org.egualpam.services.hotel.rating.domain.Location;
import org.egualpam.services.hotel.rating.domain.RatedHotel;
import org.egualpam.services.hotel.rating.domain.RatedHotelRepository;
import org.egualpam.services.hotel.rating.infrastructure.persistance.dto.Hotel;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class PostgreSqlRatedHotelRepository implements RatedHotelRepository {

    private final HotelCriteriaQueryBuilder hotelCriteriaQueryBuilder;

    public PostgreSqlRatedHotelRepository(EntityManager entityManager) {
        hotelCriteriaQueryBuilder = new HotelCriteriaQueryBuilder(entityManager);
    }

    @Override
    public List<RatedHotel> findHotelsMatchingQuery(HotelQuery hotelQuery) {

        List<Hotel> hotels = hotelCriteriaQueryBuilder.buildFrom(hotelQuery).getResultList();

        return hotels.stream()
                .map(this::mapToEntity)
                .collect(Collectors.toList());
    }

    private RatedHotel mapToEntity(Hotel hotel) {
        return new RatedHotel(
                hotel.getId().toString(),
                hotel.getName(),
                hotel.getDescription(),
                // TODO: Decide what how 'Location' will be managed (Entity or Value)
                new Location(UUID.randomUUID().toString(), hotel.getLocation()),
                hotel.getTotalPrice(),
                hotel.getImageURL()
        );
    }
}
