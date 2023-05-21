package org.egualpam.services.hotel.rating.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;
import org.egualpam.services.hotel.rating.domain.Location;
import org.egualpam.services.hotel.rating.domain.RatedHotel;
import org.egualpam.services.hotel.rating.domain.RatedHotelRepository;
import org.egualpam.services.hotel.rating.domain.Review;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RatedHotelFinderTest {

    private static final String EXPECTED_BEST_HOTEL_IDENTIFIER = "EXPECTED_BEST_HOTEL_IDENTIFIER";
    private static final String EXPECTED_INTERMEDIATE_HOTEL_IDENTIFIER =
            "EXPECTED_INTERMEDIATE_HOTEL_IDENTIFIER";
    private static final String EXPECTED_WORST_HOTEL_IDENTIFIER = "EXPECTED_WORST_HOTEL_IDENTIFIER";

    private static final HotelQuery DEFAULT_QUERY = HotelQuery.create().build();

    @Mock private RatedHotelRepository ratedHotelRepository;

    private RatedHotelFinder testee;

    @BeforeEach
    void setup() {
        testee = new RatedHotelFinder(ratedHotelRepository);
    }

    @Test
    void givenAnyQuery_hotelsMatchingQueryShouldBeReturnedSortedByRatingAverage() {
        RatedHotel expectedWorstHotel =
                buildHotelStubWithIdentifierAndReviews(
                        EXPECTED_WORST_HOTEL_IDENTIFIER,
                        List.of(buildReviewStub(1), buildReviewStub(2)));

        RatedHotel expectedIntermediateHotel =
                buildHotelStubWithIdentifierAndReviews(
                        EXPECTED_INTERMEDIATE_HOTEL_IDENTIFIER,
                        List.of(buildReviewStub(2), buildReviewStub(4)));

        RatedHotel expectedBestHotel =
                buildHotelStubWithIdentifierAndReviews(
                        EXPECTED_BEST_HOTEL_IDENTIFIER,
                        List.of(buildReviewStub(4), buildReviewStub(5)));

        when(ratedHotelRepository.findHotelsMatchingQuery(DEFAULT_QUERY))
                .thenReturn(
                        List.of(expectedIntermediateHotel, expectedWorstHotel, expectedBestHotel));

        List<RatedHotel> result = testee.findByQueryAndSortedByRatingAverage(DEFAULT_QUERY);

        assertThat(result).hasSize(3);
        assertThat(result.get(0).getIdentifier()).isEqualTo(EXPECTED_BEST_HOTEL_IDENTIFIER);
        assertThat(result.get(1).getIdentifier()).isEqualTo(EXPECTED_INTERMEDIATE_HOTEL_IDENTIFIER);
        assertThat(result.get(2).getIdentifier()).isEqualTo(EXPECTED_WORST_HOTEL_IDENTIFIER);
    }

    private RatedHotel buildHotelStubWithIdentifierAndReviews(
            String identifier, List<Review> reviews) {
        RatedHotel ratedHotel =
                new RatedHotel(
                        identifier,
                        "Amazing hotel",
                        "Eloquent description",
                        new Location("BCN", "Barcelona"),
                        200,
                        "amz-hotel-image.com");
        ratedHotel.addReviews(reviews);
        return ratedHotel;
    }

    private Review buildReviewStub(int rating) {
        return new Review("AMZ_REVIEW", rating, "Eloquent comment");
    }
}
