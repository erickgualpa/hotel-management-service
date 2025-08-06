package org.egualpam.contexts.hotel.customer.review.application.command;

import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import org.egualpam.contexts.hotel.customer.review.domain.Review;
import org.egualpam.contexts.hotel.customer.review.domain.ReviewNotFound;
import org.egualpam.contexts.hotel.customer.review.domain.ReviewUpdated;
import org.egualpam.contexts.hotel.shared.domain.AggregateId;
import org.egualpam.contexts.hotel.shared.domain.AggregateRepository;
import org.egualpam.contexts.hotel.shared.domain.DomainEvent;
import org.egualpam.contexts.hotel.shared.domain.EventBus;
import org.egualpam.contexts.hotel.shared.domain.UniqueId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UpdateReviewShould {

  private static final Instant NOW = Instant.now();

  @Mock private Clock clock;
  @Mock private Supplier<UniqueId> uniqueIdSupplier;
  @Mock private AggregateRepository<Review> reviewRepository;
  @Mock private EventBus eventBus;

  @Captor private ArgumentCaptor<Review> reviewCaptor;
  @Captor private ArgumentCaptor<Set<DomainEvent>> domainEventsCaptor;

  private UpdateReview testee;

  @BeforeEach
  void setUp() {
    testee = new UpdateReview(clock, uniqueIdSupplier, reviewRepository, eventBus);
  }

  @Test
  void updateReview() {
    String reviewId = randomUUID().toString();
    String comment = randomAlphabetic(10);
    UniqueId domainEventId = UniqueId.get();

    Review review =
        new Review(reviewId, randomUUID().toString(), nextInt(1, 5), randomAlphabetic(10));

    when(clock.instant()).thenReturn(NOW);
    when(reviewRepository.find(new AggregateId(reviewId))).thenReturn(Optional.of(review));
    when(uniqueIdSupplier.get()).thenReturn(domainEventId);

    UpdateReviewCommand command = new UpdateReviewCommand(reviewId, comment);
    testee.execute(command);

    verify(reviewRepository).save(reviewCaptor.capture());
    assertThat(reviewCaptor.getValue())
        .isNotNull()
        .satisfies(
            result -> {
              assertThat(result.comment().value()).isEqualTo(comment);
              assertThat(result.pullDomainEvents()).isEmpty();
            });

    verify(eventBus).publish(domainEventsCaptor.capture());
    assertThat(domainEventsCaptor.getValue())
        .hasSize(1)
        .first()
        .isInstanceOf(ReviewUpdated.class)
        .satisfies(
            result -> {
              assertThat(result.id()).isEqualTo(domainEventId);
              assertThat(result.aggregateId()).isEqualTo(new AggregateId(reviewId));
              assertThat(result.occurredOn()).isEqualTo(NOW);
            });
  }

  @Test
  void notUpdateReviewWhenCommentIsNotPresent() {
    String reviewId = randomUUID().toString();
    String sourceComment = randomAlphabetic(10);
    String targetComment = null;

    Review review = new Review(reviewId, randomUUID().toString(), nextInt(1, 5), sourceComment);

    when(reviewRepository.find(new AggregateId(reviewId))).thenReturn(Optional.of(review));

    UpdateReviewCommand commandWithMissingComment =
        new UpdateReviewCommand(reviewId, targetComment);

    testee.execute(commandWithMissingComment);

    verify(reviewRepository).save(reviewCaptor.capture());
    assertThat(reviewCaptor.getValue())
        .isNotNull()
        .satisfies(
            result -> {
              assertThat(result.comment().value()).isEqualTo(sourceComment);
              assertThat(result.pullDomainEvents()).isEmpty();
            });

    verify(eventBus).publish(domainEventsCaptor.capture());
    assertThat(domainEventsCaptor.getValue()).isEmpty();
  }

  @Test
  void notUpdateReviewWhenAlreadyUpdated() {
    String reviewId = randomUUID().toString();
    String comment = randomAlphabetic(10);

    Review review = new Review(reviewId, randomUUID().toString(), nextInt(1, 5), comment);

    when(reviewRepository.find(new AggregateId(reviewId))).thenReturn(Optional.of(review));

    UpdateReviewCommand command = new UpdateReviewCommand(reviewId, comment);

    testee.execute(command);

    verify(reviewRepository).save(reviewCaptor.capture());
    assertThat(reviewCaptor.getValue())
        .isNotNull()
        .satisfies(
            result -> {
              assertThat(result.comment().value()).isEqualTo(comment);
              assertThat(result.pullDomainEvents()).isEmpty();
            });

    verify(eventBus).publish(domainEventsCaptor.capture());
    assertThat(domainEventsCaptor.getValue()).isEmpty();
  }

  @Test
  void throwDomainExceptionWhenReviewNotFound() {
    String reviewId = randomUUID().toString();
    String comment = randomAlphabetic(10);

    when(reviewRepository.find(new AggregateId(reviewId))).thenReturn(Optional.empty());

    UpdateReviewCommand command = new UpdateReviewCommand(reviewId, comment);

    assertThrows(ReviewNotFound.class, () -> testee.execute(command));
  }
}
