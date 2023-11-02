package org.egualpam.services.hotel.rating.application.reviews;

import org.egualpam.services.hotel.rating.domain.reviews.Comment;
import org.egualpam.services.hotel.rating.domain.reviews.Rating;
import org.egualpam.services.hotel.rating.domain.reviews.Review;
import org.egualpam.services.hotel.rating.domain.reviews.ReviewRepository;
import org.egualpam.services.hotel.rating.domain.shared.Identifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

@ExtendWith(MockitoExtension.class)
class CreateReviewTest {

    @Captor
    private ArgumentCaptor<Review> reviewCaptor;

    @Mock
    private ReviewRepository reviewRepository;

    private CreateReview testee;

    @BeforeEach
    void setUp() {
        testee = new CreateReview(reviewRepository);
    }

    @Test
    void reviewEntityShouldBeCreated() {
        String reviewIdentifier = randomUUID().toString();
        String hotelIdentifier = randomUUID().toString();
        Integer rating = nextInt(1, 5);
        String comment = randomAlphabetic(10);

        testee.execute(
                new CreateReviewCommand(
                        reviewIdentifier,
                        hotelIdentifier,
                        rating,
                        comment
                )
        );

        verify(reviewRepository).save(reviewCaptor.capture());

        assertThat(reviewCaptor.getValue())
                .isNotNull()
                .satisfies(
                        actualReview -> {
                            assertThat(actualReview.getIdentifier()).isEqualTo(new Identifier(reviewIdentifier));
                            assertThat(actualReview.getHotelIdentifier()).isEqualTo(new Identifier(hotelIdentifier));
                            assertThat(actualReview.getRating()).isEqualTo(new Rating(rating));
                            assertThat(actualReview.getComment()).isEqualTo(new Comment(comment));
                        }
                );
    }
}