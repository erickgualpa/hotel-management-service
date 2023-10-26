package org.egualpam.services.hotel.rating.application.hotels;

import org.egualpam.services.hotel.rating.domain.hotels.Hotel;
import org.egualpam.services.hotel.rating.domain.hotels.HotelQuery;
import org.egualpam.services.hotel.rating.domain.hotels.HotelRepository;
import org.egualpam.services.hotel.rating.domain.reviews.Review;
import org.egualpam.services.hotel.rating.domain.reviews.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindHotelsByRatingAverageTest {

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @Captor
    private ArgumentCaptor<HotelQuery> hotelQueryCaptor;

    private FindHotelsByRatingAverage testee;

    @BeforeEach
    void setup() {
        testee = new FindHotelsByRatingAverage(hotelRepository, reviewRepository);
    }

    @Test
    void hotelsMatchingQueryShouldBeReturnedSortedByRatingAverage() {
        String intermediateHotelIdentifier = randomUUID().toString();
        String worstHotelIdentifier = randomUUID().toString();
        String bestHotelIdentifier = randomUUID().toString();

        when(hotelRepository.findHotelsMatchingQuery(any(HotelQuery.class)))
                .thenReturn(
                        List.of(
                                buildHotelStubWithIdentifier(intermediateHotelIdentifier),
                                buildHotelStubWithIdentifier(worstHotelIdentifier),
                                buildHotelStubWithIdentifier(bestHotelIdentifier)));

        when(reviewRepository.findByHotelIdentifier(worstHotelIdentifier))
                .thenReturn(
                        List.of(
                                buildReviewStubWithRating(1),
                                buildReviewStubWithRating(2)));

        when(reviewRepository.findByHotelIdentifier(intermediateHotelIdentifier))
                .thenReturn(
                        List.of(
                                buildReviewStubWithRating(3),
                                buildReviewStubWithRating(3)));

        when(reviewRepository.findByHotelIdentifier(bestHotelIdentifier))
                .thenReturn(
                        List.of(
                                buildReviewStubWithRating(4),
                                buildReviewStubWithRating(5)));

        List<HotelDto> result = testee.execute(Map.of());

        verify(hotelRepository).findHotelsMatchingQuery(hotelQueryCaptor.capture());
        assertThat(hotelQueryCaptor.getValue())
                .satisfies(hq -> {
                            assertNull(hq.getLocation());
                            assertNull(hq.getPriceRange());
                        }
                );

        assertThat(result).hasSize(3)
                .extracting("identifier")
                .containsExactly(
                        bestHotelIdentifier,
                        intermediateHotelIdentifier,
                        worstHotelIdentifier
                );

        assertThat(result)
                .allSatisfy(
                        hotelDto -> assertThat(hotelDto.reviews()).isNotEmpty());
    }

    private Hotel buildHotelStubWithIdentifier(String identifier) {
        return new Hotel(
                identifier,
                randomAlphabetic(5),
                randomAlphabetic(10),
                randomAlphabetic(5),
                nextInt(50, 1000),
                randomUUID().toString());
    }

    private Review buildReviewStubWithRating(int rating) {
        return new Review(randomUUID().toString(), rating, randomAlphabetic(10));
    }
}