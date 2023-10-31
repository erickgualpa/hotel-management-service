package org.egualpam.services.hotel.rating.infrastructure.persistence.jpa;

import jakarta.transaction.Transactional;
import org.egualpam.services.hotel.rating.AbstractIntegrationTest;
import org.egualpam.services.hotel.rating.domain.reviews.Comment;
import org.egualpam.services.hotel.rating.domain.reviews.Rating;
import org.egualpam.services.hotel.rating.domain.reviews.Review;
import org.egualpam.services.hotel.rating.domain.reviews.ReviewRepository;
import org.egualpam.services.hotel.rating.domain.shared.Identifier;
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
    void givenHotelIdentifier_allMatchingReviewsShouldBeReturned() {
        UUID hotelIdentifier = randomUUID();
        UUID reviewIdentifier = randomUUID();
        int rating = nextInt(1, 5);
        String comment = randomAlphabetic(10);

        persistHotelStub(hotelIdentifier);

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
                            assertThat(actualReview.getIdentifierVO())
                                    .isEqualTo(
                                            new Identifier(reviewIdentifier.toString())
                                    );
                            assertThat(actualReview.getHotelIdentifierVO())
                                    .isEqualTo(
                                            new Identifier(hotelIdentifier.toString())
                                    );
                            assertThat(actualReview.getRatingVO()).isEqualTo(new Rating(rating));
                            assertThat(actualReview.getCommentVO()).isEqualTo(new Comment(comment));
                        }
                );
    }

    @Test
    void reviewEntityShouldBeSaved() {
        UUID reviewIdentifier = randomUUID();
        UUID hotelIdentifier = randomUUID();
        int rating = nextInt(1, 5);
        String comment = randomAlphabetic(10);

        persistHotelStub(hotelIdentifier);

        Review review = new Review(
                new Identifier(reviewIdentifier.toString()),
                new Identifier(hotelIdentifier.toString()),
                new Rating(rating),
                new Comment(comment)
        );

        testee.save(review);

        org.egualpam.services.hotel.rating.infrastructure.persistence.jpa.Review result =
                testEntityManager.find(
                        org.egualpam.services.hotel.rating.infrastructure.persistence.jpa.Review.class,
                        reviewIdentifier
                );

        assertThat(result).isNotNull();
    }

    private void persistHotelStub(UUID hotelIdentifier) {
        Hotel hotel = new Hotel();
        hotel.setId(hotelIdentifier);
        hotel.setName(randomAlphabetic(5));
        hotel.setLocation(randomAlphabetic(5));
        hotel.setTotalPrice(nextInt(50, 1000));

        testEntityManager.persistAndFlush(hotel);
    }
}
