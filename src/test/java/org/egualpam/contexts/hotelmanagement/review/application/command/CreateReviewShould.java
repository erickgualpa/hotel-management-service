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

import java.time.Clock;
import java.time.Instant;
import java.util.Optional;
import java.util.Set;
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
import org.junit.jupiter.api.BeforeEach;
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

  private static final Instant NOW = Instant.now();

  @Captor private ArgumentCaptor<Review> reviewCaptor;
  @Captor private ArgumentCaptor<Set<DomainEvent>> domainEventsCaptor;

  @Mock private Clock clock;
  @Mock private AggregateRepository<Review> reviewRepository;
  @Mock private EventBus eventBus;

  private CreateReview testee;

  @BeforeEach
  void setUp() {
    testee = new CreateReview(clock, reviewRepository, eventBus);
  }

  @Test
  void createReview() {
    String reviewId = randomUUID().toString();
    String hotelIdentifier = randomUUID().toString();
    Integer rating = nextInt(1, 5);
    String comment = randomAlphabetic(10);

    when(clock.instant()).thenReturn(NOW);

    CreateReviewCommand command =
        new CreateReviewCommand(reviewId, hotelIdentifier, rating, comment);

    testee.execute(command);

    verify(reviewRepository).save(reviewCaptor.capture());
    assertThat(reviewCaptor.getValue())
        .satisfies(
            result -> {
              assertThat(result.id()).isEqualTo(new AggregateId(reviewId));
              assertThat(result.hotelId()).isEqualTo(new HotelId(hotelIdentifier));
              assertThat(result.rating()).isEqualTo(new Rating(rating));
              assertThat(result.comment()).isEqualTo(new Comment(comment));
              assertThat(result.pullDomainEvents()).isEmpty();
            });

    verify(eventBus).publish(domainEventsCaptor.capture());
    assertThat(domainEventsCaptor.getValue())
        .hasSize(1)
        .first()
        .satisfies(
            domainEvent -> {
              assertThat(domainEvent.aggregateId()).isEqualTo(new AggregateId(reviewId));
              assertThat(domainEvent.occurredOn()).isEqualTo(NOW);
            });
  }

  @Test
  void throwDomainExceptionWhenReviewAlreadyExists() {
    String reviewId = randomUUID().toString();

    Review review =
        new Review(reviewId, randomUUID().toString(), nextInt(1, 5), randomAlphabetic(10));

    when(reviewRepository.find(any(AggregateId.class))).thenReturn(Optional.of(review));

    CreateReviewCommand command =
        new CreateReviewCommand(
            reviewId, randomUUID().toString(), nextInt(1, 5), randomAlphabetic(10));

    assertThrows(ReviewAlreadyExists.class, () -> testee.execute(command));
  }

  @ValueSource(ints = {0, 6})
  @ParameterizedTest
  void throwDomainExceptionWhenRatingValueIsOutOfAllowedBounds(Integer invalidRating) {
    String reviewId = randomUUID().toString();
    String hotelIdentifier = randomUUID().toString();
    String comment = randomAlphabetic(10);

    CreateReviewCommand command =
        new CreateReviewCommand(reviewId, hotelIdentifier, invalidRating, comment);

    assertThrows(InvalidRating.class, () -> testee.execute(command));
  }

  @Test
  void throwDomainExceptionWhenReviewIdHasInvalidFormat() {
    String invalidIdentifier = randomAlphanumeric(10);
    String hotelIdentifier = randomUUID().toString();
    int rating = nextInt(1, 5);
    String comment = randomAlphabetic(10);

    CreateReviewCommand command =
        new CreateReviewCommand(invalidIdentifier, hotelIdentifier, rating, comment);

    assertThrows(InvalidUniqueId.class, () -> testee.execute(command));
  }

  @Test
  void throwDomainExceptionWhenHotelIdHasInvalidFormat() {
    String invalidIdentifier = randomAlphanumeric(10);
    String reviewId = randomUUID().toString();
    int rating = nextInt(1, 5);
    String comment = randomAlphabetic(10);

    CreateReviewCommand command =
        new CreateReviewCommand(reviewId, invalidIdentifier, rating, comment);

    assertThrows(InvalidUniqueId.class, () -> testee.execute(command));
  }

  @Test
  void throwDomainExceptionWhenReviewIdIsMissing() {
    String reviewId = null;
    String hotelId = randomUUID().toString();
    int rating = nextInt(1, 5);
    String comment = randomAlphabetic(10);

    CreateReviewCommand command = new CreateReviewCommand(reviewId, hotelId, rating, comment);

    assertThrows(RequiredPropertyIsMissing.class, () -> testee.execute(command));
  }

  @Test
  void throwDomainExceptionWhenHotelIdIsMissing() {
    String reviewId = randomUUID().toString();
    String hotelId = null;
    int rating = nextInt(1, 5);
    String comment = randomAlphabetic(10);

    CreateReviewCommand command = new CreateReviewCommand(reviewId, hotelId, rating, comment);

    assertThrows(RequiredPropertyIsMissing.class, () -> testee.execute(command));
  }

  @Test
  void throwDomainExceptionWhenRatingIsMissing() {
    String reviewId = randomUUID().toString();
    String hotelId = randomUUID().toString();
    Integer rating = null;
    String comment = randomAlphabetic(10);

    CreateReviewCommand command = new CreateReviewCommand(reviewId, hotelId, rating, comment);

    assertThrows(RequiredPropertyIsMissing.class, () -> testee.execute(command));
  }

  @Test
  void throwDomainExceptionWhenCommentIsMissing() {
    String reviewId = randomUUID().toString();
    String hotelId = randomUUID().toString();
    Integer rating = nextInt(1, 5);
    String comment = null;

    CreateReviewCommand command = new CreateReviewCommand(reviewId, hotelId, rating, comment);

    assertThrows(RequiredPropertyIsMissing.class, () -> testee.execute(command));
  }
}
