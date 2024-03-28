package org.egualpam.services.hotelmanagement.reviews.application;

import org.egualpam.services.hotelmanagement.domain.reviews.Comment;
import org.egualpam.services.hotelmanagement.domain.reviews.HotelId;
import org.egualpam.services.hotelmanagement.domain.reviews.Rating;
import org.egualpam.services.hotelmanagement.domain.reviews.Review;
import org.egualpam.services.hotelmanagement.domain.reviews.exception.InvalidRating;
import org.egualpam.services.hotelmanagement.domain.reviews.exception.ReviewAlreadyExists;
import org.egualpam.services.hotelmanagement.shared.domain.AggregateId;
import org.egualpam.services.hotelmanagement.shared.domain.AggregateRepository;
import org.egualpam.services.hotelmanagement.shared.domain.DomainEvent;
import org.egualpam.services.hotelmanagement.shared.domain.PublicEventBus;
import org.egualpam.services.hotelmanagement.shared.domain.exception.InvalidUniqueId;
import org.egualpam.services.hotelmanagement.shared.domain.exception.RequiredPropertyIsMissing;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

@ExtendWith(MockitoExtension.class)
class CreateReviewShould {

    @Captor
    private ArgumentCaptor<Review> reviewCaptor;

    @Captor
    private ArgumentCaptor<List<DomainEvent>> domainEventsCaptor;

    @Mock
    private AggregateRepository<Review> reviewRepository;

    @Mock
    private PublicEventBus publicEventBus;

    @Test
    void createReview() {
        String reviewId = randomUUID().toString();
        String hotelIdentifier = randomUUID().toString();
        Integer rating = nextInt(1, 5);
        String comment = randomAlphabetic(10);

        CreateReview testee = new CreateReview(
                reviewId,
                hotelIdentifier,
                rating,
                comment,
                reviewRepository,
                publicEventBus
        );
        testee.execute();

        verify(reviewRepository).save(reviewCaptor.capture());
        assertThat(reviewCaptor.getValue())
                .satisfies(
                        result -> {
                            assertThat(result.getId()).isEqualTo(new AggregateId(reviewId));
                            assertThat(result.getHotelId()).isEqualTo(new HotelId(hotelIdentifier));
                            assertThat(result.getRating()).isEqualTo(new Rating(rating));
                            assertThat(result.getComment()).isEqualTo(new Comment(comment));
                            assertThat(result.pullDomainEvents()).isEmpty();
                        }
                );

        verify(publicEventBus).publish(domainEventsCaptor.capture());
        assertThat(domainEventsCaptor.getValue())
                .hasSize(1)
                .first()
                .satisfies(
                        result -> {
                            assertThat(result.getAggregateId()).isEqualTo(new AggregateId(reviewId));
                            assertThat(result.getOccurredOn()).isNotNull();
                            assertThat(result.getType()).isEqualTo("domain.review.created.v1.0");
                        }
                );
    }

    @Test
    void throwDomainExceptionWhenReviewAlreadyExists() {
        String reviewId = randomUUID().toString();

        Review review = new Review(
                reviewId,
                randomUUID().toString(),
                nextInt(1, 5),
                randomAlphabetic(10)
        );

        when(reviewRepository.find(any(AggregateId.class))).thenReturn(Optional.of(review));

        CreateReview testee = new CreateReview(
                reviewId,
                randomUUID().toString(),
                nextInt(1, 5),
                randomAlphabetic(10),
                reviewRepository,
                publicEventBus
        );

        assertThrows(ReviewAlreadyExists.class, testee::execute);
    }

    @ValueSource(ints = {0, 6})
    @ParameterizedTest
    void throwDomainExceptionWhenRatingValueIsOutOfAllowedBounds(Integer invalidRating) {
        String reviewId = randomUUID().toString();
        String hotelIdentifier = randomUUID().toString();
        String comment = randomAlphabetic(10);

        CreateReview testee = new CreateReview(
                reviewId,
                hotelIdentifier,
                invalidRating,
                comment,
                reviewRepository,
                publicEventBus
        );

        assertThrows(InvalidRating.class, testee::execute);
    }

    @Test
    void throwDomainExceptionWhenReviewIdHasInvalidFormat() {
        String invalidIdentifier = randomAlphanumeric(10);
        String hotelIdentifier = randomUUID().toString();
        int rating = nextInt(1, 5);
        String comment = randomAlphabetic(10);

        CreateReview testee = new CreateReview(
                invalidIdentifier,
                hotelIdentifier,
                rating,
                comment,
                reviewRepository,
                publicEventBus
        );

        assertThrows(InvalidUniqueId.class, testee::execute);
    }

    @Test
    void throwDomainExceptionWhenHotelIdHasInvalidFormat() {
        String invalidIdentifier = randomAlphanumeric(10);
        String reviewId = randomUUID().toString();
        int rating = nextInt(1, 5);
        String comment = randomAlphabetic(10);

        CreateReview testee = new CreateReview(
                reviewId,
                invalidIdentifier,
                rating,
                comment,
                reviewRepository,
                publicEventBus
        );

        assertThrows(InvalidUniqueId.class, testee::execute);
    }

    @Test
    void throwDomainExceptionWhenReviewIdIsMissing() {
        String reviewId = null;
        String hotelId = randomUUID().toString();
        int rating = nextInt(1, 5);
        String comment = randomAlphabetic(10);

        CreateReview testee = new CreateReview(
                reviewId,
                hotelId,
                rating,
                comment,
                reviewRepository,
                publicEventBus
        );

        assertThrows(RequiredPropertyIsMissing.class, testee::execute);
    }

    @Test
    void throwDomainExceptionWhenHotelIdIsMissing() {
        String reviewId = randomUUID().toString();
        String hotelId = null;
        int rating = nextInt(1, 5);
        String comment = randomAlphabetic(10);

        CreateReview testee = new CreateReview(
                reviewId,
                hotelId,
                rating,
                comment,
                reviewRepository,
                publicEventBus
        );

        assertThrows(RequiredPropertyIsMissing.class, testee::execute);
    }

    @Test
    void throwDomainExceptionWhenRatingIsMissing() {
        String reviewId = randomUUID().toString();
        String hotelId = randomUUID().toString();
        Integer rating = null;
        String comment = randomAlphabetic(10);

        CreateReview testee = new CreateReview(
                reviewId,
                hotelId,
                rating,
                comment,
                reviewRepository,
                publicEventBus
        );

        assertThrows(RequiredPropertyIsMissing.class, testee::execute);
    }

    @Test
    void throwDomainExceptionWhenCommentIsMissing() {
        String reviewId = randomUUID().toString();
        String hotelId = randomUUID().toString();
        Integer rating = nextInt(1, 5);
        String comment = null;

        CreateReview testee = new CreateReview(
                reviewId,
                hotelId,
                rating,
                comment,
                reviewRepository,
                publicEventBus
        );

        assertThrows(RequiredPropertyIsMissing.class, testee::execute);
    }
}