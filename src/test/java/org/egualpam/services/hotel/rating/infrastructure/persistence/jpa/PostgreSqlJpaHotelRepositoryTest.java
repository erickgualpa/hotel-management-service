package org.egualpam.services.hotel.rating.infrastructure.persistence.jpa;

import jakarta.transaction.Transactional;
import org.egualpam.services.hotel.rating.AbstractIntegrationTest;
import org.egualpam.services.hotel.rating.domain.hotels.Hotel;
import org.egualpam.services.hotel.rating.domain.hotels.HotelRepository;
import org.egualpam.services.hotel.rating.domain.hotels.Location;
import org.egualpam.services.hotel.rating.domain.hotels.Price;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;
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
        String location = randomAlphabetic(5);
        int minPrice = 50;
        int maxPrice = 1000;

        org.egualpam.services.hotel.rating.infrastructure.persistence.jpa.Hotel hotel =
                new org.egualpam.services.hotel.rating.infrastructure.persistence.jpa.Hotel();
        hotel.setId(randomUUID());
        hotel.setName(randomAlphabetic(5));
        hotel.setLocation(location);
        hotel.setTotalPrice(nextInt(minPrice, maxPrice));

        testEntityManager.persistAndFlush(hotel);

        List<Hotel> result = testee.findHotels(
                Optional.of(new Location(location)),
                Optional.of(new Price(minPrice)),
                Optional.of(new Price(maxPrice))
        );

        assertThat(result).hasAtLeastOneElementOfType(Hotel.class);
    }

    @Test
    void givenQueryWithLocationFilter_matchingHotelsShouldBeReturned() {
        UUID hotelIdentifier = randomUUID();
        String location = randomAlphabetic(5);

        org.egualpam.services.hotel.rating.infrastructure.persistence.jpa.Hotel hotel =
                new org.egualpam.services.hotel.rating.infrastructure.persistence.jpa.Hotel();
        hotel.setId(hotelIdentifier);
        hotel.setName(randomAlphabetic(5));
        hotel.setLocation(location);
        hotel.setTotalPrice(nextInt(50, 1000));

        testEntityManager.persistAndFlush(hotel);

        List<Hotel> result = testee.findHotels(
                Optional.of(new Location(location)),
                Optional.empty(),
                Optional.empty()
        );

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

        org.egualpam.services.hotel.rating.infrastructure.persistence.jpa.Hotel hotel =
                new org.egualpam.services.hotel.rating.infrastructure.persistence.jpa.Hotel();
        hotel.setId(hotelIdentifier);
        hotel.setName(randomAlphabetic(5));
        hotel.setLocation(randomAlphabetic(5));
        hotel.setTotalPrice(nextInt(minPrice, maxPrice));

        testEntityManager.persistAndFlush(hotel);

        List<Hotel> result = testee.findHotels(
                Optional.empty(),
                Optional.of(new Price(minPrice)),
                Optional.of(new Price(maxPrice))
        );

        assertThat(result).hasSize(1)
                .allSatisfy(
                        actualHotel ->
                                assertThat(actualHotel.getIdentifier()).isEqualTo(hotelIdentifier.toString())
                );
    }
}