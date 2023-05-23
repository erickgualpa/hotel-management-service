package org.egualpam.services.hotel.rating.infrastructure.persistance.postgresql;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import javax.persistence.EntityManager;
import org.egualpam.services.hotel.rating.infrastructure.persistance.entity.Hotel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class PostgreSqlHotelRepositoryTest {

    @Autowired private EntityManager entityManager;
    @Autowired private PostgreSqlHotelRepository testee;

    @BeforeEach
    void setup() {
        Hotel hotel = new Hotel();
        hotel.setId(1L);
        hotel.setName("Amazing hotel");
        hotel.setDescription("This is an amazing hotel");
        hotel.setLocation("Barcelona");
        hotel.setTotalPrice(200);
        hotel.setImageURL("amz-hotel-url.com");
        testee.save(hotel);
    }

    @Test
    void givenQueryWithLocationFilter_matchingHotelsShouldBeReturned() {
        List<Hotel> result = testee.findAllByLocation("Barcelona");
        assertNotNull(result);
    }
}
