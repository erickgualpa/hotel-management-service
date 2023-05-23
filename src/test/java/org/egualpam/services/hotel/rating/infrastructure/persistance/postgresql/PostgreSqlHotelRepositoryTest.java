package org.egualpam.services.hotel.rating.infrastructure.persistance.postgresql;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import org.egualpam.services.hotel.rating.infrastructure.persistance.entity.Hotel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class PostgreSqlHotelRepositoryTest {

    @Autowired private PostgreSqlHotelRepository hotelRepository;

    // TODO: Check query 'location' and 'price-change' filtering

    @Test
    void givenQueryWithLocationFilter_matchingHotelsShouldBeReturned() {
        // TODO: Validate response is returned
        List<Hotel> result = hotelRepository.findAllByLocation("Barcelona");
        assertNotNull(result);
    }
}
