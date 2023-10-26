package org.egualpam.services.hotel.rating.infrastructure.persistence.jpa;

import jakarta.transaction.Transactional;
import org.egualpam.services.hotel.rating.AbstractIntegrationTest;
import org.egualpam.services.hotel.rating.domain.hotels.Hotel;
import org.egualpam.services.hotel.rating.domain.hotels.HotelQuery;
import org.egualpam.services.hotel.rating.domain.hotels.HotelRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.UUID;

import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@AutoConfigureTestEntityManager
class PostgreSqlJpaHotelRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private HotelRepository testee;

    @Test
    void givenEmptyQuery_allHotelsShouldBeReturned() {

        org.egualpam.services.hotel.rating.infrastructure.persistence.jpa.Hotel hotel =
                new org.egualpam.services.hotel.rating.infrastructure.persistence.jpa.Hotel();
        hotel.setId(randomUUID());
        hotel.setName(randomAlphabetic(5));
        hotel.setLocation(randomAlphabetic(5));
        hotel.setTotalPrice(nextInt(50, 1000));

        testEntityManager.persistAndFlush(hotel);

        List<Hotel> result = testee.findHotelsMatchingQuery(HotelQuery.create().build());

        assertThat(result).hasAtLeastOneElementOfType(Hotel.class);
    }

    @Test
    void givenQueryWithLocationFilter_matchingHotelsShouldBeReturned() {

        UUID hotelIdentifier = randomUUID();
        String hotelLocation = randomAlphabetic(5);

        org.egualpam.services.hotel.rating.infrastructure.persistence.jpa.Hotel hotel =
                new org.egualpam.services.hotel.rating.infrastructure.persistence.jpa.Hotel();
        hotel.setId(hotelIdentifier);
        hotel.setName(randomAlphabetic(5));
        hotel.setLocation(hotelLocation);
        hotel.setTotalPrice(nextInt(50, 1000));

        testEntityManager.persistAndFlush(hotel);

        HotelQuery hotelQuery =
                HotelQuery.create()
                        .withLocation(hotelLocation)
                        .build();

        List<Hotel> result = testee.findHotelsMatchingQuery(hotelQuery);

        assertThat(result).hasSize(1)
                .allSatisfy(
                        actualHotel ->
                                assertThat(actualHotel.getIdentifier()).isEqualTo(hotelIdentifier.toString())
                );
    }

    @Test
    void givenQueryWithPriceRangeFilter_matchingHotelsShouldBeReturned() {

        UUID hotelIdentifier = randomUUID();

        int minPrice = 50;
        int maxPrice = 500;
        int hotelTotalPrice = nextInt(minPrice, maxPrice);

        org.egualpam.services.hotel.rating.infrastructure.persistence.jpa.Hotel hotel =
                new org.egualpam.services.hotel.rating.infrastructure.persistence.jpa.Hotel();
        hotel.setId(hotelIdentifier);
        hotel.setName(randomAlphabetic(5));
        hotel.setLocation(randomAlphabetic(5));
        hotel.setTotalPrice(hotelTotalPrice);

        testEntityManager.persistAndFlush(hotel);

        List<Hotel> result = testee.findHotelsMatchingQuery(HotelQuery.create()
                .withPriceRange(minPrice, maxPrice)
                .build());

        assertThat(result).hasSize(1)
                .allSatisfy(
                        actualHotel ->
                                assertThat(actualHotel.getIdentifier()).isEqualTo(hotelIdentifier.toString())
                );
    }
}