package org.egualpam.services.hotel.rating.infrastructure.persistance;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StaticRatedHotelRepositoryTest {

    // TODO: Uncomment once amended
    private StaticRatedHotelRepository testee;
    /*
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
    }*/
}
