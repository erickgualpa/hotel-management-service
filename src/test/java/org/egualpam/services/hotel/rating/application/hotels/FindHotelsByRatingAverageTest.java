package org.egualpam.services.hotel.rating.application.hotels;

import org.egualpam.services.hotel.rating.domain.hotels.Hotel;
import org.egualpam.services.hotel.rating.domain.hotels.HotelRepository;
import org.egualpam.services.hotel.rating.domain.hotels.InvalidPriceRange;
import org.egualpam.services.hotel.rating.domain.reviews.Comment;
import org.egualpam.services.hotel.rating.domain.reviews.Rating;
import org.egualpam.services.hotel.rating.domain.reviews.Review;
import org.egualpam.services.hotel.rating.domain.reviews.ReviewRepository;
import org.egualpam.services.hotel.rating.domain.shared.Identifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
        String intermediateHotelIdentifier = randomUUID().toString();
        String worstHotelIdentifier = randomUUID().toString();
        String bestHotelIdentifier = randomUUID().toString();

        when(
                hotelRepository.findHotels(
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty()
                )
        ).thenReturn(
                List.of(
                        buildHotelStubWithIdentifier(intermediateHotelIdentifier),
                        buildHotelStubWithIdentifier(worstHotelIdentifier),
                        buildHotelStubWithIdentifier(bestHotelIdentifier)));

        when(reviewRepository.findByHotelIdentifier(new Identifier(worstHotelIdentifier)))
                .thenReturn(
                        List.of(
                                buildReviewStubWithRating(worstHotelIdentifier, 1),
                                buildReviewStubWithRating(worstHotelIdentifier, 2)));

        when(reviewRepository.findByHotelIdentifier(new Identifier(intermediateHotelIdentifier)))
                .thenReturn(
                        List.of(
                                buildReviewStubWithRating(intermediateHotelIdentifier, 3),
                                buildReviewStubWithRating(intermediateHotelIdentifier, 3)));

        when(reviewRepository.findByHotelIdentifier(new Identifier(bestHotelIdentifier)))
                .thenReturn(
                        List.of(
                                buildReviewStubWithRating(bestHotelIdentifier, 4),
                                buildReviewStubWithRating(bestHotelIdentifier, 5)));

        List<HotelDto> result = testee.execute(new Filters(null, null, null));

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

    @Test
    void invalidPriceRangeIsThrownWhenMinPriceIsGreaterThanMaxPrice() {
        Filters filters = new Filters(null, 150, 50);
        assertThrows(InvalidPriceRange.class, () -> testee.execute(filters));
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

    private Review buildReviewStubWithRating(String hotelIdentifier, int rating) {
        return new Review(
                new Identifier(randomUUID().toString()),
                new Identifier(hotelIdentifier),
                new Rating(rating),
                new Comment(randomAlphabetic(10))
        );
    }
}