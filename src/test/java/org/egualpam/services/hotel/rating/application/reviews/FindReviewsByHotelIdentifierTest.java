package org.egualpam.services.hotel.rating.application.reviews;

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

import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindReviewsByHotelIdentifierTest {

    @Mock
    private ReviewRepository reviewRepository;

    private FindReviewsByHotelIdentifier testee;

    @BeforeEach
    void setUp() {
        testee = new FindReviewsByHotelIdentifier(reviewRepository);
    }

    @Test
    void returnReviewsGivenHotelIdentifier() {
        String reviewIdentifier = randomUUID().toString();
        String hotelIdentifier = randomUUID().toString();
        int rating = nextInt(1, 5);
        String comment = randomAlphabetic(10);

        when(reviewRepository.findByHotelIdentifier(new Identifier(hotelIdentifier)))
                .thenReturn(
                        List.of(
                                new Review(
                                        new Identifier(reviewIdentifier),
                                        new Identifier(hotelIdentifier),
                                        new Rating(rating),
                                        new Comment(comment)
                                )
                        )
                );

        List<ReviewDto> result = testee.execute(hotelIdentifier);

        assertThat(result)
                .hasSize(1)
                .allSatisfy(actualReview ->
                        {
                            assertThat(actualReview.rating()).isEqualTo(rating);
                            assertThat(actualReview.comment()).isEqualTo(comment);
                        }
                );
    }
}
