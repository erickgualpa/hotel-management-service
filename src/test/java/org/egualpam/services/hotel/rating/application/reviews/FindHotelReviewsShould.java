package org.egualpam.services.hotel.rating.application.reviews;

import org.egualpam.services.hotel.rating.application.shared.InternalQuery;
import org.egualpam.services.hotel.rating.domain.reviews.Comment;
import org.egualpam.services.hotel.rating.domain.reviews.HotelId;
import org.egualpam.services.hotel.rating.domain.reviews.Rating;
import org.egualpam.services.hotel.rating.domain.reviews.Review;
import org.egualpam.services.hotel.rating.domain.shared.AggregateId;
import org.egualpam.services.hotel.rating.domain.shared.AggregateRepository;
import org.egualpam.services.hotel.rating.domain.shared.Criteria;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindHotelReviewsShould {

    @Mock
    private AggregateRepository<Review> aggregateReviewRepository;

    @Test
    void returnReviewsGivenHotelIdentifier() {
        String hotelIdentifier = randomUUID().toString();
        int rating = nextInt(1, 5);
        String comment = randomAlphabetic(10);

        when(aggregateReviewRepository.find(any(Criteria.class)))
                .thenReturn(
                        List.of(
                                new Review(
                                        new AggregateId(randomUUID()),
                                        new HotelId(hotelIdentifier),
                                        new Rating(rating),
                                        new Comment(comment)
                                )
                        )
                );

        InternalQuery<List<ReviewDto>> testee = new FindHotelReviews(
                hotelIdentifier,
                aggregateReviewRepository
        );

        List<ReviewDto> result = testee.get();

        assertThat(result)
                .hasSize(1)
                .allSatisfy(actualReview ->
                        {
                            assertThat(actualReview.rating()).isEqualTo(rating);
                            assertThat(actualReview.comment()).isEqualTo(comment);
                        }
                );
    }
}
