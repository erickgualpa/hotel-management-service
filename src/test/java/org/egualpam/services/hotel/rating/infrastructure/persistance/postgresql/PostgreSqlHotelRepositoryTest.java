package org.egualpam.services.hotel.rating.infrastructure.persistance.postgresql;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import org.egualpam.services.hotel.rating.infrastructure.persistance.entity.Hotel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest(
        properties = {
            "spring.jpa.hibernate.ddl-auto=create-drop",
            "spring.datasource.driver-class-name=com.p6spy.engine.spy.P6SpyDriver", // P6Spy
            "spring.datasource.url=jdbc:p6spy:h2:mem:testing;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=false" // P6Spy
        })
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
