package org.egualpam.services.hotel.rating.application;

import org.egualpam.services.hotel.rating.domain.Hotel;
import org.egualpam.services.hotel.rating.domain.HotelRepository;
import org.egualpam.services.hotel.rating.domain.Review;
import org.egualpam.services.hotel.rating.domain.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static java.lang.Integer.parseInt;
import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindHotelsByRatingAverageTest {

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private ReviewRepository reviewRepository;

    private FindHotelsByRatingAverage testee;

    @BeforeEach
    void setup() {
        testee = new FindHotelsByRatingAverage(hotelRepository, reviewRepository);
    }

    @Test
    void hotelsMatchingQueryShouldBeReturnedSortedByRatingAverage() {

        HotelQuery hotelQuery = HotelQuery.create().build();

        String intermediateHotelIdentifier = randomUUID().toString();
        String worstHotelIdentifier = randomUUID().toString();
        String bestHotelIdentifier = randomUUID().toString();

        when(hotelRepository.findHotelsMatchingQuery(hotelQuery))
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

        List<HotelDto> result = testee.execute(hotelQuery);

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
                parseInt(randomNumeric(3)),
                randomUUID().toString());
    }

    private Review buildReviewStubWithRating(int rating) {
        return new Review(randomUUID().toString(), rating, randomAlphabetic(10));
    }
}