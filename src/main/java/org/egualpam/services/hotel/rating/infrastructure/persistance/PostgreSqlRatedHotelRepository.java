package org.egualpam.services.hotel.rating.infrastructure.persistance;

import org.egualpam.services.hotel.rating.application.HotelQuery;
import org.egualpam.services.hotel.rating.domain.Location;
import org.egualpam.services.hotel.rating.domain.RatedHotel;
import org.egualpam.services.hotel.rating.domain.RatedHotelRepository;
import org.egualpam.services.hotel.rating.infrastructure.persistance.dto.Hotel;
import org.egualpam.services.hotel.rating.infrastructure.persistance.jpa.HotelCriteriaQueryBuilder;

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

        List<Hotel> hotels = hotelCriteriaQueryBuilder.findHotelsBy(hotelQuery);

        return hotels.stream()
                .map(this::mapToEntity)
                .collect(Collectors.toList());
    }

    private RatedHotel mapToEntity(Hotel hotel) {
        return new RatedHotel(
                hotel.id().toString(),
                hotel.name(),
                hotel.description(),
                // TODO: Decide what how 'Location' will be managed (Entity or Value)
                new Location(UUID.randomUUID().toString(), hotel.location()),
                hotel.totalPrice(),
                hotel.imageURL()
        );
    }
}
