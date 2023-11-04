package org.egualpam.services.hotel.rating.application.hotels;

import org.egualpam.services.hotel.rating.domain.hotels.Hotel;
import org.egualpam.services.hotel.rating.domain.hotels.HotelDescription;
import org.egualpam.services.hotel.rating.domain.hotels.HotelName;
import org.egualpam.services.hotel.rating.domain.hotels.HotelRepository;
import org.egualpam.services.hotel.rating.domain.hotels.HotelReview;
import org.egualpam.services.hotel.rating.domain.hotels.ImageURL;
import org.egualpam.services.hotel.rating.domain.hotels.Location;
import org.egualpam.services.hotel.rating.domain.hotels.Price;
import org.egualpam.services.hotel.rating.domain.shared.Comment;
import org.egualpam.services.hotel.rating.domain.shared.Identifier;
import org.egualpam.services.hotel.rating.domain.shared.Rating;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindHotelsByRatingAverageShould {

    @Mock
    private HotelRepository hotelRepository;

    private FindHotelsByRatingAverage testee;

    @BeforeEach
    void setup() {
        testee = new FindHotelsByRatingAverage(hotelRepository);
    }

    @Test
    void hotelsMatchingQueryShouldBeReturnedSortedByRatingAverage() {
        String intermediateHotelIdentifier = randomUUID().toString();
        String worstHotelIdentifier = randomUUID().toString();
        String bestHotelIdentifier = randomUUID().toString();

        when(
                hotelRepository.find(
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty()
                )
        ).thenReturn(
                List.of(
                        buildHotelStub(
                                new Identifier(intermediateHotelIdentifier),
                                List.of(
                                        buildReviewStub(new Rating(3)),
                                        buildReviewStub(new Rating(3))
                                )
                        ),
                        buildHotelStub(
                                new Identifier(worstHotelIdentifier),
                                List.of(
                                        buildReviewStub(new Rating(1)),
                                        buildReviewStub(new Rating(1))
                                )
                        ),
                        buildHotelStub(
                                new Identifier(bestHotelIdentifier),
                                List.of(
                                        buildReviewStub(new Rating(4)),
                                        buildReviewStub(new Rating(5))
                                )
                        )
                )
        );

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

    private Hotel buildHotelStub(Identifier identifier, List<HotelReview> reviews) {
        Hotel hotel = new Hotel(
                identifier,
                new HotelName(randomAlphabetic(5)),
                new HotelDescription(randomAlphabetic(10)),
                new Location(randomAlphabetic(5)),
                new Price(nextInt(50, 1000)),
                new ImageURL("www." + randomAlphabetic(5) + ".com")
        );
        hotel.addReviews(reviews);
        return hotel;
    }

    private HotelReview buildReviewStub(Rating rating) {
        return new HotelReview(
                rating,
                new Comment(randomAlphabetic(10))
        );
    }
}