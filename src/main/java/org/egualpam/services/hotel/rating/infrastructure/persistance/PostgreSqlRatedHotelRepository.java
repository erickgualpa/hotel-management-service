package org.egualpam.services.hotel.rating.infrastructure.persistance;

import lombok.RequiredArgsConstructor;
import org.egualpam.services.hotel.rating.application.HotelQuery;
import org.egualpam.services.hotel.rating.domain.Location;
import org.egualpam.services.hotel.rating.domain.RatedHotel;
import org.egualpam.services.hotel.rating.domain.RatedHotelRepository;
import org.egualpam.services.hotel.rating.infrastructure.persistance.dto.Hotel;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class PostgreSqlRatedHotelRepository implements RatedHotelRepository {

    private final EntityManager entityManager;

    @Override
    public List<RatedHotel> findHotelsMatchingQuery(HotelQuery hotelQuery) {

        Query query = entityManager.createNativeQuery(
                "SELECT * FROM hotels",
                Hotel.class
        );

        // TODO: Check if there is any way to do the cast in a clean way
        List<Hotel> hotels = (List<Hotel>) query.getResultList();

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
