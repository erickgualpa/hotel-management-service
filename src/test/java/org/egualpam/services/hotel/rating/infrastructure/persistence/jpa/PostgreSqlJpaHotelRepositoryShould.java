package org.egualpam.services.hotel.rating.infrastructure.persistence.jpa;

import jakarta.transaction.Transactional;
import org.egualpam.services.hotel.rating.AbstractIntegrationTest;
import org.egualpam.services.hotel.rating.domain.hotels.AverageRating;
import org.egualpam.services.hotel.rating.domain.hotels.Hotel;
import org.egualpam.services.hotel.rating.domain.hotels.HotelName;
import org.egualpam.services.hotel.rating.domain.hotels.HotelRepository;
import org.egualpam.services.hotel.rating.domain.hotels.InvalidPriceRange;
import org.egualpam.services.hotel.rating.domain.hotels.Location;
import org.egualpam.services.hotel.rating.domain.hotels.Price;
import org.egualpam.services.hotel.rating.domain.shared.Identifier;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.lang.Double.parseDouble;
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
        String hotelName = randomAlphabetic(5);
        String location = randomAlphabetic(5);
        int minPrice = 50;
        int maxPrice = 1000;
        Integer price = nextInt(minPrice, maxPrice);
        Integer rating = nextInt(1, 5);

        PersistenceHotel hotel = new PersistenceHotel();
        hotel.setId(hotelIdentifier);
        hotel.setName(hotelName);
        hotel.setLocation(location);
        hotel.setTotalPrice(price);

        PersistenceReview review = new PersistenceReview();
        review.setId(randomUUID());
        review.setRating(rating);
        review.setComment(randomAlphabetic(10));
        review.setHotelId(hotelIdentifier);

        testEntityManager.persistAndFlush(hotel);
        testEntityManager.persistAndFlush(review);

        List<Hotel> result = testee.find(
                Optional.of(new Location(location)),
                Optional.of(new Price(minPrice)),
                Optional.of(new Price(maxPrice))
        );

        assertThat(result)
                .hasSize(1)
                .allSatisfy(
                        actualHotel ->
                        {
                            assertThat(actualHotel.getIdentifier())
                                    .isEqualTo(
                                            new Identifier(hotelIdentifier.toString())
                                    );
                            assertThat(actualHotel.getName()).isEqualTo(new HotelName(hotelName));
                            assertThat(actualHotel.getLocation()).isEqualTo(new Location(location));
                            assertThat(actualHotel.getTotalPrice()).isEqualTo(new Price(price));
                            assertThat(actualHotel.getAverageRating())
                                    .isEqualTo(
                                            new AverageRating(
                                                    parseDouble(rating.toString())
                                            )
                                    );
                        }
                );
    }

    @Test
    void invalidPriceRangeShouldBeThrown_whenMinPriceFilterIsGreaterThanMaxPriceFilter() {
        Optional<Location> locationFilter = Optional.empty();
        Optional<Price> minPriceFilter = Optional.of(new Price(100));
        Optional<Price> maxPriceFilter = Optional.of(new Price(50));
        assertThrows(
                InvalidPriceRange.class,
                () -> testee.find(
                        locationFilter,
                        minPriceFilter,
                        maxPriceFilter
                )
        );
    }
}