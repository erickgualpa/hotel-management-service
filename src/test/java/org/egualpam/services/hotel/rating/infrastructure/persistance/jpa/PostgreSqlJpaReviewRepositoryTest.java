package org.egualpam.services.hotel.rating.infrastructure.persistance.jpa;

import org.egualpam.services.hotel.rating.AbstractIntegrationTest;
import org.egualpam.services.hotel.rating.domain.Review;
import org.egualpam.services.hotel.rating.domain.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DirtiesContext
public class PostgreSqlJpaReviewRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private ReviewRepository testee;

    // TODO: Clean up this test
    @Test
    void givenHotelIdentifier_allMatchingReviewShouldBeReturned() {

        UUID hotelIdentifier = UUID.randomUUID();

        String hotelQuery = """
                INSERT INTO hotels(global_identifier, name, description, location, total_price, image_url)
                VALUES (:hotelIdentifier, 'Amazing hotel', 'Eloquent description', 'Berlin', 150, 'amazing-hotel-image.com');
                """;

        String reviewQuery = """
                INSERT INTO reviews(id, rating, comment, hotel_id)
                VALUES (1, 5, 'This is an amazing hotel!', :hotelIdentifier);
                """;

        MapSqlParameterSource hotelQueryParameters = new MapSqlParameterSource();
        hotelQueryParameters.addValue("hotelIdentifier", hotelIdentifier);

        MapSqlParameterSource reviewQueryParameters = new MapSqlParameterSource();
        reviewQueryParameters.addValue("hotelIdentifier", hotelIdentifier);

        namedParameterJdbcTemplate.update(hotelQuery, hotelQueryParameters);
        namedParameterJdbcTemplate.update(reviewQuery, reviewQueryParameters);

        List<Review> result = testee.findByHotelIdentifier(hotelIdentifier.toString());

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
