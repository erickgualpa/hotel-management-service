package org.egualpam.services.hotel.rating.infrastructure.persistence.jpa;

import jakarta.transaction.Transactional;
import org.egualpam.services.hotel.rating.AbstractIntegrationTest;
import org.egualpam.services.hotel.rating.application.HotelQuery;
import org.egualpam.services.hotel.rating.domain.Hotel;
import org.egualpam.services.hotel.rating.domain.HotelRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DirtiesContext
@Transactional
@AutoConfigureTestEntityManager
public class PostgreSqlJpaHotelRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private HotelRepository testee;

    @Test
    void givenEmptyQuery_allHotelsShouldBeReturned() {

        org.egualpam.services.hotel.rating.infrastructure.persistence.jpa.Hotel hotel =
                new org.egualpam.services.hotel.rating.infrastructure.persistence.jpa.Hotel();
        hotel.setId(UUID.randomUUID());
        hotel.setName("Some random hotel");
        hotel.setLocation("Some random location");
        hotel.setTotalPrice(100);

        testEntityManager.persistAndFlush(hotel);

        List<Hotel> result = testee.findHotelsMatchingQuery(HotelQuery.create().build());

        assertThat(result).hasAtLeastOneElementOfType(Hotel.class);
    }

    @Test
    void givenQueryWithLocationFilter_matchingHotelsShouldBeReturned() {

        UUID hotelIdentifier = UUID.randomUUID();
        String hotelLocation = UUID.randomUUID().toString();

        org.egualpam.services.hotel.rating.infrastructure.persistence.jpa.Hotel hotel =
                new org.egualpam.services.hotel.rating.infrastructure.persistence.jpa.Hotel();
        hotel.setId(hotelIdentifier);
        hotel.setName("Some fake hotel name");
        hotel.setLocation(hotelLocation);
        hotel.setTotalPrice(100);

        testEntityManager.persistAndFlush(hotel);

        HotelQuery hotelQuery =
                HotelQuery.create()
                        .withLocation(hotelLocation)
                        .build();

        List<Hotel> result = testee.findHotelsMatchingQuery(hotelQuery);

        assertThat(result).hasSize(1)
                .extracting("identifier")
                .allSatisfy(
                        actualIdentifier -> assertThat(actualIdentifier).isEqualTo(hotelIdentifier.toString())
                );
    }

    @Test
    void givenQueryWithPriceRangeFilter_matchingHotelsShouldBeReturned() {

        UUID hotelIdentifier = UUID.randomUUID();
        int hotelTotalPrice = new Random().nextInt(100, 150);

        org.egualpam.services.hotel.rating.infrastructure.persistence.jpa.Hotel hotel =
                new org.egualpam.services.hotel.rating.infrastructure.persistence.jpa.Hotel();
        hotel.setId(hotelIdentifier);
        hotel.setName("Some fake hotel name");
        hotel.setLocation("Some fake location name");
        hotel.setTotalPrice(hotelTotalPrice);

        testEntityManager.persistAndFlush(hotel);

        List<Hotel> result = testee.findHotelsMatchingQuery(HotelQuery.create()
                .withPriceRange(100, 150)
                .build());

        assertThat(result).hasSize(1)
                .extracting("identifier")
                .allSatisfy(
                        actualIdentifier -> assertThat(actualIdentifier).isEqualTo(hotelIdentifier.toString())
                );
    }
}