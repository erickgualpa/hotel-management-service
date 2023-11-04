package org.egualpam.services.hotel.rating.infrastructure.persistence.jpa;

import jakarta.transaction.Transactional;
import org.egualpam.services.hotel.rating.AbstractIntegrationTest;
import org.egualpam.services.hotel.rating.domain.hotels.Hotel;
import org.egualpam.services.hotel.rating.domain.hotels.HotelRepository;
import org.egualpam.services.hotel.rating.domain.hotels.InvalidPriceRange;
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
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@AutoConfigureTestEntityManager
class PostgreSqlJpaHotelRepositoryShould extends AbstractIntegrationTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private HotelRepository testee;

    @Test
    void hotelsMatchingFiltersShouldBeReturned() {
        UUID hotelIdentifier = randomUUID();
        String location = randomAlphabetic(5);
        int minPrice = 50;
        int maxPrice = 1000;

        PersistenceHotel hotel = new PersistenceHotel();
        hotel.setId(hotelIdentifier);
        hotel.setName(randomAlphabetic(5));
        hotel.setLocation(location);
        hotel.setTotalPrice(nextInt(minPrice, maxPrice));

        PersistenceReview review = new PersistenceReview();
        review.setId(randomUUID());
        review.setRating(nextInt(1, 5));
        review.setComment(randomAlphabetic(10));
        review.setHotelId(hotelIdentifier);

        testEntityManager.persistAndFlush(hotel);
        testEntityManager.persistAndFlush(review);

        List<Hotel> result = testee.findHotels(
                Optional.of(new Location(location)),
                Optional.of(new Price(minPrice)),
                Optional.of(new Price(maxPrice))
        );

        assertThat(result)
                .hasSize(1)
                .allSatisfy(
                        actualHotels -> assertThat(actualHotels.getReviews()).hasSize(1)
                );
    }

    @Test
    void invalidPriceRangeShouldBeThrown_whenMinPriceFilterIsGreaterThanMaxPriceFilter() {
        Optional<Location> locationFilter = Optional.empty();
        Optional<Price> minPriceFilter = Optional.of(new Price(100));
        Optional<Price> maxPriceFilter = Optional.of(new Price(50));
        assertThrows(
                InvalidPriceRange.class,
                () -> testee.findHotels(
                        locationFilter,
                        minPriceFilter,
                        maxPriceFilter
                )
        );
    }
}