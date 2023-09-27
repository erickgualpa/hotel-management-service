package org.egualpam.services.hotel.rating.infrastructure.persistance.jpa;

import org.egualpam.services.hotel.rating.AbstractIntegrationTest;
import org.egualpam.services.hotel.rating.domain.Review;
import org.egualpam.services.hotel.rating.domain.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DirtiesContext
@Transactional
@AutoConfigureTestEntityManager
public class PostgreSqlJpaReviewRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private ReviewRepository testee;

    @Test
    void givenHotelIdentifier_allMatchingReviewShouldBeReturned() {

        UUID hotelIdentifier = UUID.randomUUID();
        UUID reviewIdentifier = UUID.randomUUID();

        Hotel hotel = new Hotel();
        hotel.setId(hotelIdentifier);
        hotel.setName("Some fake hotel name");
        hotel.setLocation("Some fake location name");
        hotel.setTotalPrice(100);

        testEntityManager.persistAndFlush(hotel);

        org.egualpam.services.hotel.rating.infrastructure.persistance.jpa.Review review =
                new org.egualpam.services.hotel.rating.infrastructure.persistance.jpa.Review();
        review.setId(reviewIdentifier);
        review.setRating(4);
        review.setComment("This is a nice hotel!");
        review.setHotel_id(hotelIdentifier);

        testEntityManager.persistAndFlush(review);

        List<Review> result = testee.findByHotelIdentifier(hotelIdentifier.toString());

        assertThat(result)
                .hasSize(1)
                .allSatisfy(
                        actualReview -> {
                            assertThat(actualReview.getIdentifier()).isEqualTo(reviewIdentifier.toString());
                            assertThat(actualReview.getRating()).isEqualTo(4);
                            assertThat(actualReview.getComment())
                                    .isEqualTo("This is a nice hotel!");
                        }
                );
    }
}
