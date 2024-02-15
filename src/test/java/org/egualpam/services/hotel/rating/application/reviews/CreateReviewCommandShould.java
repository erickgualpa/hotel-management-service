package org.egualpam.services.hotel.rating.application.reviews;

import org.egualpam.services.hotel.rating.domain.reviews.Review;
import org.egualpam.services.hotel.rating.domain.reviews.ReviewRepository;
import org.egualpam.services.hotel.rating.domain.shared.Comment;
import org.egualpam.services.hotel.rating.domain.shared.Identifier;
import org.egualpam.services.hotel.rating.domain.shared.InvalidIdentifier;
import org.egualpam.services.hotel.rating.domain.shared.InvalidRating;
import org.egualpam.services.hotel.rating.domain.shared.Rating;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

@ExtendWith(MockitoExtension.class)
class CreateReviewCommandShould {

    @Captor
    private ArgumentCaptor<Review> reviewCaptor;

    @Mock
    private ReviewRepository reviewRepository;

    private CreateReviewCommand testee;

    @Test
    void givenReviewShouldBeSaved() {
        String reviewIdentifier = randomUUID().toString();
        String hotelIdentifier = randomUUID().toString();
        Integer rating = nextInt(1, 5);
        String comment = randomAlphabetic(10);

        testee = new CreateReviewCommand(
                reviewIdentifier,
                hotelIdentifier,
                rating,
                comment,
                reviewRepository
        );
        testee.execute();

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

    @ValueSource(ints = {0, 6})
    @ParameterizedTest
    void invalidRatingShouldBeThrown_whenRatingValueIsOutOfAllowedBounds(Integer invalidRating) {
        testee = new CreateReviewCommand(
                randomUUID().toString(),
                randomUUID().toString(),
                invalidRating,
                randomAlphabetic(10),
                reviewRepository
        );
        assertThrows(InvalidRating.class, () -> testee.execute());
    }

    @Test
    void invalidIdentifierShouldBeThrown_whenReviewIdentifierHasInvalidFormat() {
        String invalidIdentifier = randomAlphanumeric(10);
        testee = new CreateReviewCommand(
                invalidIdentifier,
                randomUUID().toString(),
                nextInt(1, 5),
                randomAlphabetic(10),
                reviewRepository
        );
        assertThrows(InvalidIdentifier.class, () -> testee.execute());
    }

    @Test
    void invalidIdentifierShouldBeThrown_whenHotelIdentifierHasInvalidFormat() {
        String invalidIdentifier = randomAlphanumeric(10);
        testee = new CreateReviewCommand(
                randomUUID().toString(),
                invalidIdentifier,
                nextInt(1, 5),
                randomAlphabetic(10),
                reviewRepository
        );
        assertThrows(InvalidIdentifier.class, () -> testee.execute());
    }
}