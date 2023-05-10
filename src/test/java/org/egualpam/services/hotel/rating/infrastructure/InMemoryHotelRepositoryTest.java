package org.egualpam.services.hotel.rating.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.egualpam.services.hotel.rating.application.HotelQuery;
import org.egualpam.services.hotel.rating.domain.RatedHotel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InMemoryHotelRepositoryTest {

    private InMemoryHotelRepository testee;

    @BeforeEach
    void setup() {
        testee = new InMemoryHotelRepository();
    }

    @Test
    void givenHotelQuery_matchingHotelsShouldBeReturned() {
        HotelQuery query =
                HotelQuery.create().withLocation("Barcelona").withPriceRange(100, 200).build();

        List<RatedHotel> result = testee.findHotelsMatchingQuery(query);

        assertThat(result).isNotEmpty();
    }

    @Test
    void givenHotelQuerySpecifyingNonMatchingPriceRange_noHotelsShouldBeReturned() {
        HotelQuery query = HotelQuery.create().withLocation("UNMATCHING_LOCATION").build();

        List<RatedHotel> result = testee.findHotelsMatchingQuery(query);

        assertThat(result).isEmpty();
    }
}
