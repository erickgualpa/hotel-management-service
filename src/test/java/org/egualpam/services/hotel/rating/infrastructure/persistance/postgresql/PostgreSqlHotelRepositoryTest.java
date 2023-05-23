package org.egualpam.services.hotel.rating.infrastructure.persistance.postgresql;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.egualpam.services.hotel.rating.application.HotelQuery;
import org.egualpam.services.hotel.rating.infrastructure.persistance.entity.Hotel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class PostgreSqlHotelRepositoryTest {

    @Autowired private PostgreSqlHotelRepository testee;

    @BeforeEach
    void setup() {
        assertThat(testee.count()).isZero();
    }

    @Test
    void givenQueryWithLocationFilter_matchingHotelsShouldBeReturned() {
        Hotel hotel = buildHotelWithIdentifier(1L);
        testee.save(hotel);
        List<Hotel> result = testee.findAllByLocation("Barcelona");
        assertThat(result).isNotEmpty();
    }

    @Test
    void givenQueryWithPriceRangeFilter_matchingHotelsShouldBeReturned() {
        Hotel hotel = buildHotelWithIdentifier(1L);
        testee.save(hotel);
        List<Hotel> result = testee.findByPriceRange(100, 300);
        assertThat(result).isNotEmpty();
    }

    @Test
    void givenQueryWithMultipleFilters_matchingHotelsShouldBeReturned() {
        Hotel hotel = buildHotelWithIdentifier(1L);
        testee.save(hotel);

        Hotel expensivehotel = buildHotelWithIdentifier(2L);
        expensivehotel.setTotalPrice(100000);
        testee.save(expensivehotel);

        HotelQuery query =
                HotelQuery.create().withLocation("Barcelona").withPriceRange(100, 500).build();
        List<Hotel> result = testee.findHotelsMatchingQuery(query);

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
