package org.egualpam.services.hotel.rating.infrastructure.persistance;

import org.egualpam.services.hotel.rating.application.HotelQuery;
import org.egualpam.services.hotel.rating.infrastructure.persistance.dto.Hotel;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

// TODO: Cleanup this testing class
// @DataJpaTest
class HotelQueryRepositoryTest {

    /*@Autowired */private HotelQueryRepository testee;

    // TODO: Disable test once this approach is retaken
    @Test
    @Disabled
    void givenQueryWithMultipleFilters_matchingHotelsShouldBeReturned() {
        Hotel hotel = buildHotelWithIdentifier(1L);
        testee.registerHotel(hotel);

        Hotel expensivehotel = buildHotelWithIdentifier(2L);
        expensivehotel.setTotalPrice(100000);
        testee.registerHotel(expensivehotel);

        HotelQuery hotelQuery =
                HotelQuery.create().withLocation("Barcelona").withPriceRange(100, 500).build();
        List<Hotel> result = testee.findHotelsMatchingQuery(hotelQuery);

        assertThat(result).hasSize(1);
    }

    private Hotel buildHotelWithIdentifier(Long identifier) {
        Hotel hotel = new Hotel();
        hotel.setId(identifier);
        hotel.setName("Amazing hotel");
        hotel.setDescription("This is an amazing hotel");
        hotel.setLocation("Barcelona");
        hotel.setTotalPrice(200);
        hotel.setImageURL("amz-hotel-url.com");
        return hotel;
    }
}
