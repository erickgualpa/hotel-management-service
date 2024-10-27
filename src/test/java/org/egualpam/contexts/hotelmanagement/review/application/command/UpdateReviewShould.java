package org.egualpam.contexts.hotelmanagement.review.application.command;

import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.egualpam.contexts.hotelmanagement.review.domain.Review;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateId;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateRepository;
import org.egualpam.contexts.hotelmanagement.shared.domain.DomainEvent;
import org.egualpam.contexts.hotelmanagement.shared.domain.EventBus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UpdateReviewShould {

  @Mock private AggregateRepository<Review> reviewRepository;

  @Mock private EventBus eventBus;

  @Captor private ArgumentCaptor<Review> reviewCaptor;

  @Captor private ArgumentCaptor<List<DomainEvent>> domainEventsCaptor;

  @Test
  void updateReview() {
    String reviewId = randomUUID().toString();
    String comment = randomAlphabetic(10);

    Review review =
        new Review(reviewId, randomUUID().toString(), nextInt(1, 5), randomAlphabetic(10));

    when(reviewRepository.find(new AggregateId(reviewId))).thenReturn(Optional.of(review));

    UpdateReview testee = new UpdateReview(reviewId, comment, reviewRepository, eventBus);

    testee.execute();

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
        .satisfies(
            result -> {
              assertThat(result.getAggregateId()).isEqualTo(new AggregateId(reviewId));
              assertThat(result.getOccurredOn()).isNotNull();
            });
  }
}
