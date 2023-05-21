package org.egualpam.services.hotel.rating.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.egualpam.services.hotel.rating.infrastructure.entity.Review;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StaticReviewRepositoryTest {

    private StaticReviewRepository testee;

    @BeforeEach
    void setup() {
        testee = new StaticReviewRepository();
    }

    @Test
    void givenHotelIdentifier_matchingReviewsShouldBeReturned() {
        List<Review> result = testee.findReviewsMatchingHotelIdentifier("AMZ_HOTEL");
        assertThat(result).isNotEmpty();
    }
}
