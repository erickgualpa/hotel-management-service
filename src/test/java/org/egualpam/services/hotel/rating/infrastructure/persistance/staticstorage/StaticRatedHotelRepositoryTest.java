package org.egualpam.services.hotel.rating.infrastructure.persistance.staticstorage;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.egualpam.services.hotel.rating.application.HotelQuery;
import org.egualpam.services.hotel.rating.domain.RatedHotel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StaticRatedHotelRepositoryTest {

    private StaticRatedHotelRepository testee;

    @BeforeEach
    void setup() {
        testee =
                new StaticRatedHotelRepository(
                        new StaticHotelRepository(), new StaticReviewRepository());
    }

    @Test
    void givenAnyQuery_ratedHotelsShouldBeReturned() {
        List<RatedHotel> result = testee.findHotelsMatchingQuery(HotelQuery.create().build());
        assertThat(result)
                .hasSize(5)
                .allSatisfy(
                        actualRatedHotel -> assertThat(actualRatedHotel.getReviews()).isNotEmpty());
    }
}
