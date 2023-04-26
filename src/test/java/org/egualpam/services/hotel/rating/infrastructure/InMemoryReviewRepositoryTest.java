package org.egualpam.services.hotel.rating.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.egualpam.services.hotel.rating.domain.HotelReview;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InMemoryReviewRepositoryTest {

    private InMemoryReviewRepository testee;

    @BeforeEach
    void setup() {
        testee = new InMemoryReviewRepository();
    }

    @Test
    void givenHotelIdentifier_matchingReviewsShouldBeReturned() {
        List<HotelReview> result = testee.findReviewsMatchingHotelIdentifier("AMZ_HOTEL");
        assertThat(result).isNotEmpty();
    }
}
