package org.egualpam.services.hotel.rating.application.reviews;

import org.egualpam.services.hotel.rating.application.shared.InternalQuery;
import org.egualpam.services.hotel.rating.application.shared.ViewSupplier;
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
    private ViewSupplier<ReviewsView> reviewsViewSupplier;

    @Test
    void getReviewsViewGivenHotelIdentifier() {
        String hotelId = randomUUID().toString();
        int rating = nextInt(1, 5);
        String comment = randomAlphabetic(10);

        when(reviewsViewSupplier.get(any(Criteria.class)))
                .thenReturn(
                        new ReviewsView(List.of(
                                new ReviewsView.Review(rating, comment)
                        ))
                );

        InternalQuery<ReviewsView> testee = new FindHotelReviews(
                hotelId,
                reviewsViewSupplier
        );

        ReviewsView result = testee.get();

        assertThat(result.reviews())
                .hasSize(1)
                .first()
                .satisfies(actual ->
                        {
                            assertThat(actual.rating()).isEqualTo(rating);
                            assertThat(actual.comment()).isEqualTo(comment);
                        }
                );
    }
}
