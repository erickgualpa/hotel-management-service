package org.egualpam.contexts.hotelmanagement.review.application.command;

import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

import java.util.List;
import java.util.Optional;
import org.egualpam.contexts.hotelmanagement.review.domain.Comment;
import org.egualpam.contexts.hotelmanagement.review.domain.HotelId;
import org.egualpam.contexts.hotelmanagement.review.domain.InvalidRating;
import org.egualpam.contexts.hotelmanagement.review.domain.Rating;
import org.egualpam.contexts.hotelmanagement.review.domain.Review;
import org.egualpam.contexts.hotelmanagement.review.domain.ReviewAlreadyExists;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateId;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateRepository;
import org.egualpam.contexts.hotelmanagement.shared.domain.DomainEvent;
import org.egualpam.contexts.hotelmanagement.shared.domain.EventBus;
import org.egualpam.contexts.hotelmanagement.shared.domain.InvalidUniqueId;
import org.egualpam.contexts.hotelmanagement.shared.domain.RequiredPropertyIsMissing;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateReviewShould {

  @Captor private ArgumentCaptor<Review> reviewCaptor;

  @Captor private ArgumentCaptor<List<DomainEvent>> domainEventsCaptor;

  @Mock private AggregateRepository<Review> reviewRepository;

  @Mock private EventBus eventBus;

  @Test
  void createReview() {
    String reviewId = randomUUID().toString();
    String hotelIdentifier = randomUUID().toString();
    Integer rating = nextInt(1, 5);
    String comment = randomAlphabetic(10);

    CreateReview testee =
        new CreateReview(reviewId, hotelIdentifier, rating, comment, reviewRepository, eventBus);
    testee.execute();

    verify(reviewRepository).save(reviewCaptor.capture());
    assertThat(reviewCaptor.getValue())
        .satisfies(
            result -> {
              assertThat(result.id()).isEqualTo(new AggregateId(reviewId));
              assertThat(result.getHotelId()).isEqualTo(new HotelId(hotelIdentifier));
              assertThat(result.getRating()).isEqualTo(new Rating(rating));
              assertThat(result.getComment()).isEqualTo(new Comment(comment));
              assertThat(result.pullDomainEvents()).isEmpty();
            });

    verify(eventBus).publish(domainEventsCaptor.capture());
    assertThat(domainEventsCaptor.getValue())
        .hasSize(1)
        .first()
        .satisfies(
            result -> {
              assertThat(result.getAggregateId()).isEqualTo(new AggregateId(reviewId));
              assertThat(result.getOccurredOn()).isNotNull();
            });
  }

  @Test
  void throwDomainExceptionWhenReviewAlreadyExists() {
    String reviewId = randomUUID().toString();

    Review review =
        new Review(reviewId, randomUUID().toString(), nextInt(1, 5), randomAlphabetic(10));

    when(reviewRepository.find(any(AggregateId.class))).thenReturn(Optional.of(review));

    CreateReview testee =
        new CreateReview(
            reviewId,
            randomUUID().toString(),
            nextInt(1, 5),
            randomAlphabetic(10),
            reviewRepository,
            eventBus);

    assertThrows(ReviewAlreadyExists.class, testee::execute);
  }

  @ValueSource(ints = {0, 6})
  @ParameterizedTest
  void throwDomainExceptionWhenRatingValueIsOutOfAllowedBounds(Integer invalidRating) {
    String reviewId = randomUUID().toString();
    String hotelIdentifier = randomUUID().toString();
    String comment = randomAlphabetic(10);

    CreateReview testee =
        new CreateReview(
            reviewId, hotelIdentifier, invalidRating, comment, reviewRepository, eventBus);

    assertThrows(InvalidRating.class, testee::execute);
  }

  @Test
  void throwDomainExceptionWhenReviewIdHasInvalidFormat() {
    String invalidIdentifier = randomAlphanumeric(10);
    String hotelIdentifier = randomUUID().toString();
    int rating = nextInt(1, 5);
    String comment = randomAlphabetic(10);

    CreateReview testee =
        new CreateReview(
            invalidIdentifier, hotelIdentifier, rating, comment, reviewRepository, eventBus);

    assertThrows(InvalidUniqueId.class, testee::execute);
  }

  @Test
  void throwDomainExceptionWhenHotelIdHasInvalidFormat() {
    String invalidIdentifier = randomAlphanumeric(10);
    String reviewId = randomUUID().toString();
    int rating = nextInt(1, 5);
    String comment = randomAlphabetic(10);

    CreateReview testee =
        new CreateReview(reviewId, invalidIdentifier, rating, comment, reviewRepository, eventBus);

    assertThrows(InvalidUniqueId.class, testee::execute);
  }

  @Test
  void throwDomainExceptionWhenReviewIdIsMissing() {
    String reviewId = null;
    String hotelId = randomUUID().toString();
    int rating = nextInt(1, 5);
    String comment = randomAlphabetic(10);

    CreateReview testee =
        new CreateReview(reviewId, hotelId, rating, comment, reviewRepository, eventBus);

    assertThrows(RequiredPropertyIsMissing.class, testee::execute);
  }

  @Test
  void throwDomainExceptionWhenHotelIdIsMissing() {
    String reviewId = randomUUID().toString();
    String hotelId = null;
    int rating = nextInt(1, 5);
    String comment = randomAlphabetic(10);

    CreateReview testee =
        new CreateReview(reviewId, hotelId, rating, comment, reviewRepository, eventBus);

    assertThrows(RequiredPropertyIsMissing.class, testee::execute);
  }

  @Test
  void throwDomainExceptionWhenRatingIsMissing() {
    String reviewId = randomUUID().toString();
    String hotelId = randomUUID().toString();
    Integer rating = null;
    String comment = randomAlphabetic(10);

    CreateReview testee =
        new CreateReview(reviewId, hotelId, rating, comment, reviewRepository, eventBus);

    assertThrows(RequiredPropertyIsMissing.class, testee::execute);
  }

  @Test
  void throwDomainExceptionWhenCommentIsMissing() {
    String reviewId = randomUUID().toString();
    String hotelId = randomUUID().toString();
    Integer rating = nextInt(1, 5);
    String comment = null;

    CreateReview testee =
        new CreateReview(reviewId, hotelId, rating, comment, reviewRepository, eventBus);

    assertThrows(RequiredPropertyIsMissing.class, testee::execute);
  }
}
