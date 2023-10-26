package org.egualpam.services.hotel.rating.application;

import org.egualpam.services.hotel.rating.domain.reviews.Review;
import org.egualpam.services.hotel.rating.domain.reviews.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindReviewsTest {

    @Mock
    private ReviewRepository reviewRepository;

    private FindReviews testee;

    @BeforeEach
    void setUp() {
        testee = new FindReviews(reviewRepository);
    }

    @Test
    void returnReviewsGivenHotelIdentifier() {

        String hotelIdentifier = randomUUID().toString();

        int rating = nextInt(1, 5);
        String comment = randomAlphabetic(10);

        when(reviewRepository.findByHotelIdentifier(hotelIdentifier))
                .thenReturn(
                        List.of(
                                new Review(
                                        randomUUID().toString(),
                                        rating,
                                        comment
                                )
                        ));

        List<ReviewDto> result = testee.findByHotelIdentifier(hotelIdentifier);

        assertThat(result).hasSize(1)
                .allSatisfy(actualReview ->
                        {
                            assertThat(actualReview.rating()).isEqualTo(rating);
                            assertThat(actualReview.comment()).isEqualTo(comment);
                        }
                );
    }
}
