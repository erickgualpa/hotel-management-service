package org.egualpam.services.hotel.rating.infrastructure.persistence.jpa;

import jakarta.transaction.Transactional;
import org.egualpam.services.hotel.rating.AbstractIntegrationTest;
import org.egualpam.services.hotel.rating.domain.Review;
import org.egualpam.services.hotel.rating.domain.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.UUID;

import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@AutoConfigureTestEntityManager
class PostgreSqlJpaReviewRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private ReviewRepository testee;

    @Test
    void givenHotelIdentifier_allMatchingReviewShouldBeReturned() {

        UUID hotelIdentifier = randomUUID();
        UUID reviewIdentifier = randomUUID();
        int rating = nextInt(1, 5);
        String comment = randomAlphabetic(10);

        Hotel hotel = new Hotel();
        hotel.setId(hotelIdentifier);
        hotel.setName(randomAlphabetic(5));
        hotel.setLocation(randomAlphabetic(5));
        hotel.setTotalPrice(nextInt(50, 1000));

        testEntityManager.persistAndFlush(hotel);

        org.egualpam.services.hotel.rating.infrastructure.persistence.jpa.Review review =
                new org.egualpam.services.hotel.rating.infrastructure.persistence.jpa.Review();
        review.setId(reviewIdentifier);
        review.setRating(rating);
        review.setComment(comment);
        review.setHotelId(hotelIdentifier);

        testEntityManager.persistAndFlush(review);

        List<Review> result = testee.findByHotelIdentifier(hotelIdentifier.toString());

        assertThat(result)
                .hasSize(1)
                .allSatisfy(
                        actualReview -> {
                            assertThat(actualReview.getIdentifier()).isEqualTo(reviewIdentifier.toString());
                            assertThat(actualReview.getRating()).isEqualTo(rating);
                            assertThat(actualReview.getComment()).isEqualTo(comment);
                        }
                );
    }
}
