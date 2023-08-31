package org.egualpam.services.hotel.rating.infrastructure.persistance;

import org.egualpam.services.hotel.rating.application.HotelQuery;
import org.egualpam.services.hotel.rating.domain.RatedHotel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class StaticRatedHotelRepositoryTest {

    private StaticRatedHotelRepository testee;

    @BeforeEach
    void setup() {
        testee = new StaticRatedHotelRepository();
    }

    @Test
    void givenAnyQuery_fakeRatedHotelShouldBeReturned() {

        List<RatedHotel> result = testee.findHotelsMatchingQuery(HotelQuery.create().build());

        assertThat(result)
                .hasSize(1)
                .allSatisfy(
                        actualRatedHotel -> {

                            assertThat(actualRatedHotel.getIdentifier()).isEqualTo("some-hotel-identifier");
                            assertThat(actualRatedHotel.getName()).isEqualTo("some-hotel-name");
                            assertThat(actualRatedHotel.getDescription()).isEqualTo("some-hotel-description");

                            assertThat(actualRatedHotel.getLocation())
                                    .satisfies(actualLocation -> {

                                        assertThat(actualLocation.getIdentifier())
                                                .isEqualTo("some-location-identifier");

                                        assertThat(actualLocation.getName()).isEqualTo("some-location-name");
                                    });

                            assertThat(actualRatedHotel.getTotalPrice()).isEqualTo(100);
                            assertThat(actualRatedHotel.getImageURL()).isEqualTo("some-image-url");

                            assertThat(actualRatedHotel.getReviews())
                                    .hasSize(1)
                                    .allSatisfy(actualReview -> {

                                        assertThat(actualReview.getIdentifier())
                                                .isEqualTo("some-review-identifier");

                                        assertThat(actualReview.getRating()).isEqualTo(3);

                                        assertThat(actualReview.getComment())
                                                .isEqualTo("some-review-comment");
                                    });
                        });
    }
}
