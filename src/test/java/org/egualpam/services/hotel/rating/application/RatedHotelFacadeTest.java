package org.egualpam.services.hotel.rating.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import org.egualpam.services.hotel.rating.controller.HotelQuery;
import org.egualpam.services.hotel.rating.domain.Hotel;
import org.egualpam.services.hotel.rating.domain.HotelLocation;
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

    @Mock private HotelRepository hotelRepository;
    @Mock private ReviewRepository reviewRepository;

    private RatedHotelFacade testee;

    @BeforeEach
    void setup() {
        testee = new RatedHotelFacade(hotelRepository, reviewRepository);
    }

    @Test
    void givenAnyQuery_hotelsMatchingQueryShouldBeReturned() {
        Hotel defaultHotel =
                new Hotel(
                        EXPECTED_HOTEL_IDENTIFIER,
                        EXPECTED_NAME,
                        EXPECTED_DESCRIPTION,
                        EXPECTED_LOCATION,
                        EXPECTED_TOTAL_PRICE,
                        EXPECTED_IMAGE_URL);

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
        Hotel defaultHotel =
                new Hotel(
                        EXPECTED_HOTEL_IDENTIFIER,
                        EXPECTED_NAME,
                        EXPECTED_DESCRIPTION,
                        EXPECTED_LOCATION,
                        EXPECTED_TOTAL_PRICE,
                        EXPECTED_IMAGE_URL);

        when(hotelRepository.findHotelsMatchingQuery(any(HotelQuery.class)))
                .thenReturn(List.of(defaultHotel));
        when(reviewRepository.findReviewsMatchingHotelIdentifier(EXPECTED_HOTEL_IDENTIFIER))
                .thenReturn(List.of(new Review()));

        List<RatedHotel> result = testee.findHotelsMatchingQuery(new HotelQuery());

        assertThat(result).hasSize(1);
        RatedHotel actualHotel = result.get(0);
        assertThat(actualHotel.getReviews()).hasSize(1);
    }
}