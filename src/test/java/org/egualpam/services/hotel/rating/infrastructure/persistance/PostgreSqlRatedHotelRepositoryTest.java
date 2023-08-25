package org.egualpam.services.hotel.rating.infrastructure.persistance;

import org.egualpam.services.hotel.rating.application.HotelQuery;
import org.egualpam.services.hotel.rating.domain.RatedHotel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PostgreSqlRatedHotelRepositoryTest {

    private PostgreSqlRatedHotelRepository testee;

    @BeforeEach
    void setUp() {
        testee = new PostgreSqlRatedHotelRepository();
    }

    @Test
    void givenAnyQuery_matchingRatedHotelsShouldBeReturned() {
        List<RatedHotel> result = testee.findHotelsMatchingQuery(HotelQuery.create().build());
        assertThat(result).isEmpty();
    }
}
