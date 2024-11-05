package org.egualpam.contexts.hotelmanagement.review.application.query;

import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import org.egualpam.contexts.hotelmanagement.review.domain.ReviewCriteria;
import org.egualpam.contexts.hotelmanagement.shared.application.query.ReadModelSupplier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FindReviewsShould {

  @Mock private ReadModelSupplier<ReviewCriteria, ManyReviews> readModelSupplier;

  private FindReviews testee;

  @BeforeEach
  void setUp() {
    testee = new FindReviews(readModelSupplier);
  }

  @Test
  void findReviews() {
    String hotelId = randomUUID().toString();
    int rating = 2;
    String comment = randomAlphabetic(10);

    ManyReviews.Review review = new ManyReviews.Review(rating, comment);
    ManyReviews manyReviews = new ManyReviews(List.of(review));
    when(readModelSupplier.get(any(ReviewCriteria.class))).thenReturn(manyReviews);

    FindReviewsQuery query = new FindReviewsQuery(hotelId);

    ManyReviews result = testee.execute(query);

    ManyReviews.Review expected = new ManyReviews.Review(rating, comment);
    assertThat(result.reviews()).hasSize(1).first().isEqualTo(expected);
  }

  @Test
  void findEmptyWhenNoReviewsFound() {
    String hotelId = randomUUID().toString();

    ManyReviews emptyReviews = new ManyReviews(List.of());
    when(readModelSupplier.get(any(ReviewCriteria.class))).thenReturn(emptyReviews);

    FindReviewsQuery query = new FindReviewsQuery(hotelId);

    ManyReviews result = testee.execute(query);

    assertThat(result.reviews()).isEmpty();
  }
}
