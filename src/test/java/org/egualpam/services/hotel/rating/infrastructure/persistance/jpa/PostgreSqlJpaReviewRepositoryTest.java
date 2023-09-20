package org.egualpam.services.hotel.rating.infrastructure.persistance.jpa;

import org.egualpam.services.hotel.rating.AbstractIntegrationTest;
import org.egualpam.services.hotel.rating.domain.Review;
import org.egualpam.services.hotel.rating.domain.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DirtiesContext
public class PostgreSqlJpaReviewRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private ReviewRepository testee;

    @Test
    @Sql(statements = """
            INSERT INTO hotels(id, name, description, location, total_price, image_url)
            VALUES (91, 'Amazing hotel', 'Eloquent description', 'Berlin', 150, 'amazing-hotel-image.com');
            INSERT INTO reviews(id, rating, comment, hotel_id)
            VALUES (1, 5, 'This is an amazing hotel!', 91);
            """)
    void givenHotelIdentifier_allMatchingReviewShouldBeReturned() {

        List<Review> result = testee.findByHotelIdentifier("91");

        assertThat(result)
                .hasSize(1)
                .allSatisfy(
                        review -> {
                            assertThat(review.getIdentifier()).isEqualTo("1");
                            assertThat(review.getRating()).isEqualTo(5);
                            assertThat(review.getComment())
                                    .isEqualTo("This is an amazing hotel!");
                        }
                );
    }
}
