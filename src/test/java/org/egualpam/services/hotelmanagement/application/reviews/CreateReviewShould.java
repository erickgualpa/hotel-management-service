package org.egualpam.services.hotelmanagement.application.reviews;

import org.egualpam.services.hotelmanagement.domain.reviews.Comment;
import org.egualpam.services.hotelmanagement.domain.reviews.HotelId;
import org.egualpam.services.hotelmanagement.domain.reviews.Rating;
import org.egualpam.services.hotelmanagement.domain.reviews.Review;
import org.egualpam.services.hotelmanagement.domain.reviews.exception.InvalidRating;
import org.egualpam.services.hotelmanagement.domain.reviews.exception.ReviewAlreadyExists;
import org.egualpam.services.hotelmanagement.domain.shared.AggregateId;
import org.egualpam.services.hotelmanagement.domain.shared.AggregateRepository;
import org.egualpam.services.hotelmanagement.domain.shared.DomainEvent;
import org.egualpam.services.hotelmanagement.domain.shared.DomainEventsPublisher;
import org.egualpam.services.hotelmanagement.domain.shared.exception.InvalidUniqueId;
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
    private AggregateRepository<Review> aggregateReviewRepository;

    @Mock
    private DomainEventsPublisher domainEventsPublisher;

    @Test
    void givenReviewShouldBeSaved() {
        String reviewId = randomUUID().toString();
        String hotelIdentifier = randomUUID().toString();
        Integer rating = nextInt(1, 5);
        String comment = randomAlphabetic(10);

        CreateReview testee = new CreateReview(
                reviewId,
                hotelIdentifier,
                rating,
                comment,
                aggregateReviewRepository,
                domainEventsPublisher
        );
        testee.execute();

        verify(aggregateReviewRepository).save(reviewCaptor.capture());
        assertThat(reviewCaptor.getValue())
                .satisfies(
                        result -> {
                            assertThat(result.getId()).isEqualTo(new AggregateId(reviewId));
                            assertThat(result.getHotelIdentifier()).isEqualTo(new HotelId(hotelIdentifier));
                            assertThat(result.getRating()).isEqualTo(new Rating(rating));
                            assertThat(result.getComment()).isEqualTo(new Comment(comment));
                            assertThat(result.pullDomainEvents()).isEmpty();
                        }
                );

        verify(domainEventsPublisher).publish(domainEventsCaptor.capture());
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
                new AggregateId(reviewId),
                new HotelId(randomUUID().toString()),
                new Rating(nextInt(1, 5)),
                new Comment(randomAlphabetic(10))
        );

        when(aggregateReviewRepository.find(any(AggregateId.class))).thenReturn(Optional.of(review));

        CreateReview testee = new CreateReview(
                reviewId,
                randomUUID().toString(),
                nextInt(1, 5),
                randomAlphabetic(10),
                aggregateReviewRepository,
                domainEventsPublisher
        );

        assertThrows(ReviewAlreadyExists.class, testee::execute);
    }

    @ValueSource(ints = {0, 6})
    @ParameterizedTest
    void domainExceptionShouldBeThrown_whenRatingValueIsOutOfAllowedBounds(Integer invalidRating) {
        String reviewId = randomUUID().toString();
        String hotelIdentifier = randomUUID().toString();
        String comment = randomAlphabetic(10);
        assertThrows(
                InvalidRating.class,
                () -> new CreateReview(
                        reviewId,
                        hotelIdentifier,
                        invalidRating,
                        comment,
                        aggregateReviewRepository,
                        domainEventsPublisher
                )
        );
    }

    @Test
    void domainExceptionShouldBeThrown_whenReviewIdHasInvalidFormat() {
        String invalidIdentifier = randomAlphanumeric(10);
        String hotelIdentifier = randomUUID().toString();
        int rating = nextInt(1, 5);
        String comment = randomAlphabetic(10);
        assertThrows(
                InvalidUniqueId.class,
                () -> new CreateReview(
                        invalidIdentifier,
                        hotelIdentifier,
                        rating,
                        comment,
                        aggregateReviewRepository,
                        domainEventsPublisher
                )
        );
    }

    @Test
    void domainExceptionShouldBeThrown_whenHotelIdentifierHasInvalidFormat() {
        String invalidIdentifier = randomAlphanumeric(10);
        String reviewId = randomUUID().toString();
        int rating = nextInt(1, 5);
        String comment = randomAlphabetic(10);
        assertThrows(
                InvalidUniqueId.class,
                () -> new CreateReview(
                        reviewId,
                        invalidIdentifier,
                        rating,
                        comment,
                        aggregateReviewRepository,
                        domainEventsPublisher
                )
        );
    }
}