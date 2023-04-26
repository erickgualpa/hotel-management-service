package org.egualpam.services.hotel.rating.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import org.egualpam.services.hotel.rating.controller.HotelQuery;
import org.egualpam.services.hotel.rating.domain.HotelLocation;
import org.egualpam.services.hotel.rating.domain.HotelReview;
import org.egualpam.services.hotel.rating.domain.HotelReviewRepository;
import org.egualpam.services.hotel.rating.domain.RatedHotel;
import org.egualpam.services.hotel.rating.domain.RatedHotelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RatedHotelFacadeTest {

    private static final HotelLocation EXPECTED_LOCATION = new HotelLocation("BCN", "Barcelona");

    private static final String EXPECTED_HOTEL_IDENTIFIER = "AMZ_HOTEL";
    private static final String EXPECTED_NAME = "Amazing hotel";
    private static final String EXPECTED_DESCRIPTION = "Eloquent description";
    private static final String EXPECTED_IMAGE_URL = "amz-hotel-image.com";

    private static final Integer EXPECTED_TOTAL_PRICE = 200;
    private static final String EXPECTED_REVIEW_IDENTIFIER = "AMZ_REVIEW";
    private static final int EXPECTED_REVIEW_RATING = 5;
    private static final String EXPECTED_REVIEW_COMMENT = "Eloquent comment";

    private static final String EXPECTED_WORST_HOTEL_IDENTIFIER = "EXPECTED_WORST_HOTEL_IDENTIFIER";
    private static final String EXPECTED_INTERMEDIATE_HOTEL_IDENTIFIER =
            "EXPECTED_INTERMEDIATE_HOTEL_IDENTIFIER";
    private static final String EXPECTED_BEST_HOTEL_IDENTIFIER = "EXPECTED_BEST_HOTEL_IDENTIFIER";

    @Mock private RatedHotelRepository hotelRepository;
    @Mock private HotelReviewRepository reviewRepository;

    private RatedHotelFacade testee;

    @BeforeEach
    void setup() {
        testee = new RatedHotelFacade(hotelRepository, reviewRepository);
    }

    @Test
    void givenAnyQuery_hotelsMatchingQueryShouldBeReturned() {
        RatedHotel defaultHotel = buildHotelStub(EXPECTED_HOTEL_IDENTIFIER);

        when(hotelRepository.findHotelsMatchingQuery(any(HotelQuery.class)))
                .thenReturn(List.of(defaultHotel));

        List<RatedHotel> result = testee.findHotelsMatchingQuery(new HotelQuery());

        assertThat(result).hasSize(1);
        RatedHotel actualHotel = result.get(0);
        assertThat(actualHotel.getIdentifier()).isEqualTo(EXPECTED_HOTEL_IDENTIFIER);
        assertThat(actualHotel.getName()).isEqualTo(EXPECTED_NAME);
        assertThat(actualHotel.getDescription()).isEqualTo(EXPECTED_DESCRIPTION);
        assertThat(actualHotel.getLocation()).isEqualTo(EXPECTED_LOCATION);
        assertThat(actualHotel.getTotalPrice()).isEqualTo(EXPECTED_TOTAL_PRICE);
        assertThat(actualHotel.getImageURL()).isEqualTo(EXPECTED_IMAGE_URL);
    }

    @Test
    void givenAnyQuery_hotelsMatchingQueryShouldBePopulatedWithReviewsAndReturned() {
        RatedHotel defaultHotel = buildHotelStub(EXPECTED_HOTEL_IDENTIFIER);
        HotelReview defaultReview = buildReviewStub(EXPECTED_REVIEW_RATING);

        when(hotelRepository.findHotelsMatchingQuery(any(HotelQuery.class)))
                .thenReturn(List.of(defaultHotel));
        when(reviewRepository.findReviewsMatchingHotelIdentifier(EXPECTED_HOTEL_IDENTIFIER))
                .thenReturn(List.of(defaultReview));

        List<RatedHotel> result = testee.findHotelsMatchingQuery(new HotelQuery());

        assertThat(result).hasSize(1);
        RatedHotel actualHotel = result.get(0);
        assertThat(actualHotel.getReviews()).hasSize(1);
        HotelReview actualReview = actualHotel.getReviews().get(0);
        assertThat(actualReview.getIdentifier()).isEqualTo(EXPECTED_REVIEW_IDENTIFIER);
        assertThat(actualReview.getRating()).isEqualTo(EXPECTED_REVIEW_RATING);
        assertThat(actualReview.getComment()).isEqualTo(EXPECTED_REVIEW_COMMENT);
    }

    @Test
    void givenAnyQuery_hotelsMatchingQueryShouldBeReturnedOrderedByRatingAverage() {
        RatedHotel expectedWorstHotel = buildHotelStub(EXPECTED_WORST_HOTEL_IDENTIFIER);
        List<HotelReview> expectedWorstHotelReviews =
                List.of(buildReviewStub(1), buildReviewStub(2));

        RatedHotel expectedIntermediateHotel =
                buildHotelStub(EXPECTED_INTERMEDIATE_HOTEL_IDENTIFIER);
        List<HotelReview> expectedIntermediateHotelReviews =
                List.of(buildReviewStub(2), buildReviewStub(4));

        RatedHotel expectedBestHotel = buildHotelStub(EXPECTED_BEST_HOTEL_IDENTIFIER);
        List<HotelReview> expectedBestHotelReviews =
                List.of(buildReviewStub(4), buildReviewStub(5));

        when(hotelRepository.findHotelsMatchingQuery(any(HotelQuery.class)))
                .thenReturn(
                        List.of(expectedIntermediateHotel, expectedWorstHotel, expectedBestHotel));

        when(reviewRepository.findReviewsMatchingHotelIdentifier(EXPECTED_WORST_HOTEL_IDENTIFIER))
                .thenReturn(expectedWorstHotelReviews);
        when(reviewRepository.findReviewsMatchingHotelIdentifier(
                        EXPECTED_INTERMEDIATE_HOTEL_IDENTIFIER))
                .thenReturn(expectedIntermediateHotelReviews);
        when(reviewRepository.findReviewsMatchingHotelIdentifier(EXPECTED_BEST_HOTEL_IDENTIFIER))
                .thenReturn(expectedBestHotelReviews);

        List<RatedHotel> result = testee.findHotelsMatchingQuery(new HotelQuery());

        assertThat(result).hasSize(3);
        assertThat(result.get(0).getIdentifier()).isEqualTo(EXPECTED_BEST_HOTEL_IDENTIFIER);
        assertThat(result.get(1).getIdentifier()).isEqualTo(EXPECTED_INTERMEDIATE_HOTEL_IDENTIFIER);
        assertThat(result.get(2).getIdentifier()).isEqualTo(EXPECTED_WORST_HOTEL_IDENTIFIER);
    }

    private RatedHotel buildHotelStub(String identifier) {
        return new RatedHotel(
                identifier,
                EXPECTED_NAME,
                EXPECTED_DESCRIPTION,
                EXPECTED_LOCATION,
                EXPECTED_TOTAL_PRICE,
                EXPECTED_IMAGE_URL);
    }

    private HotelReview buildReviewStub(int rating) {
        return new HotelReview(EXPECTED_REVIEW_IDENTIFIER, rating, EXPECTED_REVIEW_COMMENT);
    }
}
