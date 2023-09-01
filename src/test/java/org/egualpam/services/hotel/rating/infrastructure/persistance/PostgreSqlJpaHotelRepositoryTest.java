package org.egualpam.services.hotel.rating.infrastructure.persistance;

import org.egualpam.services.hotel.rating.AbstractIntegrationTest;
import org.egualpam.services.hotel.rating.application.HotelQuery;
import org.egualpam.services.hotel.rating.domain.RatedHotel;
import org.egualpam.services.hotel.rating.domain.RatedHotelRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DirtiesContext
public class PostgreSqlJpaHotelRepositoryTest extends AbstractIntegrationTest {

    private static final int NON_MATCHING_HOTEL_PRICE_VALUE = 10000000;

    @Autowired
    private RatedHotelRepository testee;

    /* TODO: The data present in database should be controlled by each test
     *           - Currently single record shared by all tests
     * */

    @Test
    void givenQueryWithNoFilters_allResultsShouldBeReturned() {

        HotelQuery hotelQuery = HotelQuery.create().build();

        List<RatedHotel> result = testee.findHotelsMatchingQuery(hotelQuery);

        assertThat(result).hasSize(1)
                .allSatisfy(
                        actualHotel ->
                        {
                            assertThat(actualHotel.getIdentifier()).isEqualTo("1");
                            assertThat(actualHotel.getName()).isEqualTo("Amazing hotel");
                            assertThat(actualHotel.getDescription()).isEqualTo("Eloquent description");
                            assertThat(actualHotel.getTotalPrice()).isEqualTo(150);
                            assertThat(actualHotel.getImageURL()).isEqualTo("amazing-hotel-image.com");

                            assertThat(actualHotel.getLocation()).satisfies(
                                    actualHotelLocation -> {
                                        assertNotNull(actualHotelLocation.getIdentifier());
                                        assertThat(actualHotelLocation.getName()).isEqualTo("Barcelona");
                                    });
                        }

                );
    }

    @Test
    void givenQueryWithLocationFilter_matchingHotelsShouldBeReturned() {

        HotelQuery hotelQuery =
                HotelQuery.create()
                        .withLocation("Barcelona")
                        .build();

        List<RatedHotel> result = testee.findHotelsMatchingQuery(hotelQuery);

        assertThat(result).hasSize(1);
    }

    @Test
    void givenQueryWithNonMatchingLocationFilter_noHotelsShouldBeReturned() {

        HotelQuery hotelQuery =
                HotelQuery.create()
                        .withLocation(UUID.randomUUID().toString())
                        .build();

        List<RatedHotel> result = testee.findHotelsMatchingQuery(hotelQuery);

        assertThat(result).isEmpty();
    }

    @Test
    void givenQueryWithPriceRangeFilter_matchingHotelsShouldBeReturned() {

        HotelQuery hotelQuery =
                HotelQuery.create()
                        .withPriceRange(0, 500)
                        .build();

        List<RatedHotel> result = testee.findHotelsMatchingQuery(hotelQuery);

        assertThat(result).hasSize(1);
    }

    @Test
    void givenQueryWithNonMatchingPriceRangeFilter_noHotelsShouldBeReturned() {

        HotelQuery hotelQuery =
                HotelQuery.create()
                        .withPriceRange(NON_MATCHING_HOTEL_PRICE_VALUE, NON_MATCHING_HOTEL_PRICE_VALUE)
                        .build();

        List<RatedHotel> result = testee.findHotelsMatchingQuery(hotelQuery);

        assertThat(result).isEmpty();
    }
}
