package org.egualpam.services.hotel.rating.application;

import org.egualpam.services.hotel.rating.domain.Hotel;
import org.egualpam.services.hotel.rating.domain.HotelRepository;
import org.egualpam.services.hotel.rating.domain.Location;
import org.egualpam.services.hotel.rating.domain.Review;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindHotelsByRatingAverageTest {

    private static final String EXPECTED_BEST_HOTEL_IDENTIFIER = "EXPECTED_BEST_HOTEL_IDENTIFIER";
    private static final String EXPECTED_INTERMEDIATE_HOTEL_IDENTIFIER =
            "EXPECTED_INTERMEDIATE_HOTEL_IDENTIFIER";
    private static final String EXPECTED_WORST_HOTEL_IDENTIFIER = "EXPECTED_WORST_HOTEL_IDENTIFIER";

    private static final HotelQuery DEFAULT_QUERY = HotelQuery.create().build();

    @Mock
    private HotelRepository hotelRepository;

    private FindHotelsByRatingAverage testee;

    @BeforeEach
    void setup() {
        testee = new FindHotelsByRatingAverage(hotelRepository);
    }

    @Test
    void givenAnyQuery_hotelsMatchingQueryShouldBeReturnedSortedByRatingAverage() {
        Hotel expectedWorstHotel =
                buildHotelStubWithIdentifierAndReviews(
                        EXPECTED_WORST_HOTEL_IDENTIFIER,
                        List.of(buildReviewStub(1), buildReviewStub(2)));

        Hotel expectedIntermediateHotel =
                buildHotelStubWithIdentifierAndReviews(
                        EXPECTED_INTERMEDIATE_HOTEL_IDENTIFIER,
                        List.of(buildReviewStub(2), buildReviewStub(4)));

        Hotel expectedBestHotel =
                buildHotelStubWithIdentifierAndReviews(
                        EXPECTED_BEST_HOTEL_IDENTIFIER,
                        List.of(buildReviewStub(4), buildReviewStub(5)));

        when(hotelRepository.findHotelsMatchingQuery(DEFAULT_QUERY))
                .thenReturn(
                        List.of(expectedIntermediateHotel, expectedWorstHotel, expectedBestHotel));

        List<HotelDto> result = testee.execute(DEFAULT_QUERY);

        assertThat(result).hasSize(3)
                .extracting("identifier")
                .containsExactly(
                        EXPECTED_BEST_HOTEL_IDENTIFIER,
                        EXPECTED_INTERMEDIATE_HOTEL_IDENTIFIER,
                        EXPECTED_WORST_HOTEL_IDENTIFIER
                );
    }

    private Hotel buildHotelStubWithIdentifierAndReviews(
            String identifier, List<Review> reviews) {
        Hotel hotel =
                new Hotel(
                        identifier,
                        "Amazing hotel",
                        "Eloquent description",
                        new Location("BCN", "Barcelona"),
                        200,
                        "amz-hotel-image.com");
        hotel.addReviews(reviews);
        return hotel;
    }

    private Review buildReviewStub(int rating) {
        return new Review("AMZ_REVIEW", rating, "Eloquent comment");
    }
}
