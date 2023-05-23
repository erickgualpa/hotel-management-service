package org.egualpam.services.hotel.rating.infrastructure.persistance.postgresql;

import java.util.List;
import org.egualpam.services.hotel.rating.infrastructure.persistance.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostgreSqlHotelRepository extends JpaRepository<Hotel, Long>, HotelQueryRepository {

    List<Hotel> findAllByLocation(String location);

    @Query(
            value =
                    "SELECT * "
                            + "FROM hotels h "
                            + "WHERE h.total_price > ?1 "
                            + "AND h.total_price < ?2",
            nativeQuery = true)
    List<Hotel> findByPriceRange(Integer min, Integer max);
}
