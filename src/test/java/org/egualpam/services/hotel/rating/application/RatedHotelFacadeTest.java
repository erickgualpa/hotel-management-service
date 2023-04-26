package org.egualpam.services.hotel.rating.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import org.egualpam.services.hotel.rating.controller.HotelQuery;
import org.egualpam.services.hotel.rating.domain.Hotel;
import org.egualpam.services.hotel.rating.domain.HotelLocation;
import org.egualpam.services.hotel.rating.domain.HotelReview;
import org.egualpam.services.hotel.rating.domain.RatedHotel;
import org.egualpam.services.hotel.rating.domain.Review;
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
    public static final String EXPECTED_REVIEW_IDENTIFIER = "AMZ_REVIEW";
    public static final int EXPECTED_REVIEW_RATING = 5;
    public static final String EXPECTED_REVIEW_COMMENT = "Eloquent comment";
    public static final String EXPECTED_WORST_HOTEL_IDENTIFIER = "EXPECTED_WORST_HOTEL_IDENTIFIER";
    public static final String EXPECTED_INTERMEDIATE_HOTEL_IDENTIFIER =
            "EXPECTED_INTERMEDIATE_HOTEL_IDENTIFIER";
    public static final String EXPECTED_BEST_HOTEL_IDENTIFIER = "EXPECTED_BEST_HOTEL_IDENTIFIER";

    @Mock private HotelRepository hotelRepository;
    @Mock private ReviewRepository reviewRepository;

    private RatedHotelFacade testee;

    @BeforeEach
    void setup() {
        testee = new RatedHotelFacade(hotelRepository, reviewRepository);
    }

    @Test
    void givenAnyQuery_hotelsMatchingQueryShouldBeReturned() {
        Hotel defaultHotel = buildHotelStub(EXPECTED_HOTEL_IDENTIFIER);

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
        Hotel defaultHotel = buildHotelStub(EXPECTED_HOTEL_IDENTIFIER);

        Review defaultReview = buildReviewStub(EXPECTED_REVIEW_RATING, EXPECTED_HOTEL_IDENTIFIER);

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
        Hotel expectedWorstHotel = buildHotelStub(EXPECTED_WORST_HOTEL_IDENTIFIER);
        List<Review> expectedWorstHotelReviews =
                List.of(
                        buildReviewStub(1, EXPECTED_WORST_HOTEL_IDENTIFIER),
                        buildReviewStub(2, EXPECTED_WORST_HOTEL_IDENTIFIER));

        Hotel expectedIntermediateHotel = buildHotelStub(EXPECTED_INTERMEDIATE_HOTEL_IDENTIFIER);
        List<Review> expectedIntermediateHotelReviews =
                List.of(
                        buildReviewStub(2, EXPECTED_INTERMEDIATE_HOTEL_IDENTIFIER),
                        buildReviewStub(4, EXPECTED_INTERMEDIATE_HOTEL_IDENTIFIER));

        Hotel expectedBestHotel = buildHotelStub(EXPECTED_BEST_HOTEL_IDENTIFIER);
        List<Review> expectedBestHotelReviews =
                List.of(
                        buildReviewStub(4, EXPECTED_BEST_HOTEL_IDENTIFIER),
                        buildReviewStub(5, EXPECTED_BEST_HOTEL_IDENTIFIER));

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

    private Hotel buildHotelStub(String identifier) {
        return new Hotel(
                identifier,
                EXPECTED_NAME,
                EXPECTED_DESCRIPTION,
                EXPECTED_LOCATION,
                EXPECTED_TOTAL_PRICE,
                EXPECTED_IMAGE_URL);
    }

    private Review buildReviewStub(int rating, String hotelIdentifier) {
        return new Review(
                EXPECTED_REVIEW_IDENTIFIER, rating, EXPECTED_REVIEW_COMMENT, hotelIdentifier);
    }
}
