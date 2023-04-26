package org.egualpam.services.hotel.rating.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import org.egualpam.services.hotel.rating.controller.HotelQuery;
import org.egualpam.services.hotel.rating.domain.Hotel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
                new HotelQuery(
                        "Barcelona",
                        LocalDate.parse("2023-06-24"),
                        LocalDate.parse("2023-06-28"),
                        new HotelQuery.PriceRange(100, 200));

        List<Hotel> result = testee.findHotelsMatchingQuery(query);

        assertThat(result).isNotEmpty();
    }

    @Test
    @Disabled
    void givenHotelQuerySpecifyingNonMatchingPriceRange_noHotelsShouldBeReturned() {
        HotelQuery query =
                new HotelQuery(
                        "Barcelona",
                        LocalDate.parse("2023-06-24"),
                        LocalDate.parse("2023-06-28"),
                        new HotelQuery.PriceRange(50, 100));

        List<Hotel> result = testee.findHotelsMatchingQuery(query);

        assertThat(result).isEmpty();
    }
}
