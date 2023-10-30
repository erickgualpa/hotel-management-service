package org.egualpam.services.hotel.rating.application.reviews;

import org.egualpam.services.hotel.rating.domain.reviews.Review;
import org.egualpam.services.hotel.rating.domain.reviews.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

@ExtendWith(MockitoExtension.class)
class CreateReviewTest {

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
        Integer randomRating = nextInt(1, 5);
        String comment = randomAlphabetic(10);

        testee.execute(reviewIdentifier, randomRating, comment);

        verify(reviewRepository).save(any(Review.class));
    }
}