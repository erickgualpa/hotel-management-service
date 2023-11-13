package org.egualpam.services.hotel.rating.infrastructure.persistence.jpa;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.egualpam.services.hotel.rating.AbstractIntegrationTest;
import org.egualpam.services.hotel.rating.domain.reviews.Review;
import org.egualpam.services.hotel.rating.domain.reviews.ReviewRepository;
import org.egualpam.services.hotel.rating.domain.shared.Comment;
import org.egualpam.services.hotel.rating.domain.shared.Identifier;
import org.egualpam.services.hotel.rating.domain.shared.Rating;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.UUID;

import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.assertj.core.api.Assertions.assertThat;

@TestConfiguration
class PostgreSqlJpaReviewRepositoryTestConfiguration {

    @Bean
    public ReviewRepository testee(EntityManager entityManager) {
        return new PostgreSqlJpaReviewRepository(entityManager);
    }
}

@Transactional
@AutoConfigureTestEntityManager
@Import(PostgreSqlJpaHotelRepositoryTestConfiguration.class)
class PostgreSqlJpaReviewRepositoryShould extends AbstractIntegrationTest {

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

        PersistenceReview review = new PersistenceReview();
        review.setId(reviewIdentifier);
        review.setRating(rating);
        review.setComment(comment);
        review.setHotelId(hotelIdentifier);

        testEntityManager.persistAndFlush(review);

        List<Review> result = testee.findByHotelIdentifier(
                new Identifier(hotelIdentifier.toString())
        );

        assertThat(result)
                .hasSize(1)
                .allSatisfy(
                        actualReview -> {
                            assertThat(actualReview.getIdentifier())
                                    .isEqualTo(
                                            new Identifier(reviewIdentifier.toString())
                                    );
                            assertThat(actualReview.getHotelIdentifier())
                                    .isEqualTo(
                                            new Identifier(hotelIdentifier.toString())
                                    );
                            assertThat(actualReview.getRating()).isEqualTo(new Rating(rating));
                            assertThat(actualReview.getComment()).isEqualTo(new Comment(comment));
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

        PersistenceReview result =
                testEntityManager.find(
                        PersistenceReview.class,
                        reviewIdentifier
                );

        assertThat(result).isNotNull();
    }

    private void persistHotelStub(UUID hotelIdentifier) {
        PersistenceHotel hotel = new PersistenceHotel();
        hotel.setId(hotelIdentifier);
        hotel.setName(randomAlphabetic(5));
        hotel.setLocation(randomAlphabetic(5));
        hotel.setTotalPrice(nextInt(50, 1000));

        testEntityManager.persistAndFlush(hotel);
    }
}
