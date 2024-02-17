package org.egualpam.services.hotel.rating.application.reviews;

import org.egualpam.services.hotel.rating.domain.reviews.Review;
import org.egualpam.services.hotel.rating.domain.reviews.ReviewRepository;
import org.egualpam.services.hotel.rating.domain.shared.Comment;
import org.egualpam.services.hotel.rating.domain.shared.Identifier;
import org.egualpam.services.hotel.rating.domain.shared.Rating;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateReviewShould {

    @Mock
    private ReviewRepository reviewRepository;

    @Captor
    private ArgumentCaptor<Review> reviewCaptor;

    @Test
    void updateReview() {
        String reviewIdentifier = randomUUID().toString();
        String comment = randomAlphabetic(10);

        when(reviewRepository.findByIdentifier(new Identifier(reviewIdentifier)))
                .thenReturn(
                        new Review(
                                new Identifier(reviewIdentifier),
                                new Identifier(randomUUID().toString()),
                                new Rating(nextInt(1, 5)),
                                new Comment(randomAlphabetic(10))
                        )
                );

        UpdateReview testee = new UpdateReview(
                reviewIdentifier,
                comment,
                reviewRepository
        );

        testee.execute();

        verify(reviewRepository).save(reviewCaptor.capture());
        assertThat(reviewCaptor.getValue())
                .isNotNull()
                .satisfies(
                        result -> assertThat(result.getComment().value()).isEqualTo(comment)
                );
    }
}