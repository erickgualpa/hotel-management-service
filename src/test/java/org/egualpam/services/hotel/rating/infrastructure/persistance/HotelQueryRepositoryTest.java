package org.egualpam.services.hotel.rating.infrastructure.persistance;

import org.egualpam.services.hotel.rating.AbstractIntegrationTest;
import org.egualpam.services.hotel.rating.application.HotelQuery;
import org.egualpam.services.hotel.rating.infrastructure.persistance.dto.Hotel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DirtiesContext
class HotelQueryRepositoryTest extends AbstractIntegrationTest {

    private static final int NON_MATCHING_HOTEL_PRICE_VALUE = 1000000;

    @Autowired
    private HotelQueryRepositoryImpl testee;

    @Test
    void givenQueryWithLocationFilter_matchingHotelsShouldBeReturned() {
        HotelQuery hotelQuery =
                HotelQuery.create()
                        .withLocation("Barcelona")
                        .build();

        List<Hotel> result = testee.findHotelsMatchingQuery(hotelQuery);

        assertThat(result).hasSize(1);
    }

    @Test
    void givenQueryWithNonMatchingLocationFilter_noHotelsShouldBeReturned() {
        HotelQuery hotelQuery =
                HotelQuery.create()
                        .withLocation(UUID.randomUUID().toString())
                        .build();

        List<Hotel> result = testee.findHotelsMatchingQuery(hotelQuery);

        assertThat(result).isEmpty();
    }

    @Test
    void givenQueryWithPriceRangeFilter_matchingHotelsShouldBeReturned() {
        HotelQuery hotelQuery =
                HotelQuery.create()
                        .withPriceRange(0, 500)
                        .build();

        List<Hotel> result = testee.findHotelsMatchingQuery(hotelQuery);

        assertThat(result).hasSize(1);
    }

    @Test
    void givenQueryWithNonMatchingPriceRangeFilter_noHotelsShouldBeReturned() {
        HotelQuery hotelQuery =
                HotelQuery.create()
                        .withPriceRange(NON_MATCHING_HOTEL_PRICE_VALUE, NON_MATCHING_HOTEL_PRICE_VALUE)
                        .build();

        List<Hotel> result = testee.findHotelsMatchingQuery(hotelQuery);

        assertThat(result).isEmpty();
    }
}
