package org.egualpam.services.hotel.rating.application.reviews;

import org.egualpam.services.hotel.rating.domain.reviews.Comment;
import org.egualpam.services.hotel.rating.domain.reviews.Rating;
import org.egualpam.services.hotel.rating.domain.reviews.Review;
import org.egualpam.services.hotel.rating.domain.shared.AggregateId;
import org.egualpam.services.hotel.rating.domain.shared.AggregateRepository;
import org.egualpam.services.hotel.rating.domain.shared.Identifier;
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
    private AggregateRepository<Review> aggregateReviewRepository;

    @Captor
    private ArgumentCaptor<Review> reviewCaptor;

    @Test
    void updateReview() {
        String reviewId = randomUUID().toString();
        String comment = randomAlphabetic(10);

        when(aggregateReviewRepository.find(new AggregateId(reviewId)))
                .thenReturn(
                        new Review(
                                new Identifier(reviewId),
                                new Identifier(randomUUID().toString()),
                                new Rating(nextInt(1, 5)),
                                new Comment(randomAlphabetic(10))
                        )
                );

        UpdateReview testee = new UpdateReview(
                reviewId,
                comment,
                aggregateReviewRepository
        );

        testee.execute();

        verify(aggregateReviewRepository).save(reviewCaptor.capture());
        assertThat(reviewCaptor.getValue())
                .isNotNull()
                .satisfies(
                        result -> assertThat(result.getComment().value()).isEqualTo(comment)
                );
    }
}