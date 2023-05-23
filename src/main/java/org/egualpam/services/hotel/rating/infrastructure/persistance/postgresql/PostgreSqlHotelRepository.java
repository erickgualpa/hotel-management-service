package org.egualpam.services.hotel.rating.infrastructure.persistance.postgresql;

import java.util.List;
import org.egualpam.services.hotel.rating.infrastructure.persistance.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostgreSqlHotelRepository extends JpaRepository<Hotel, Long> {
    List<Hotel> findAllByLocation(String location);
}
