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

        insertHotelWithIdentifier(hotelIdentifier);
        insertReviewWithHotelIdentifier(hotelIdentifier);

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

    private void insertHotelWithIdentifier(UUID hotelIdentifier) {
        String query = """
                INSERT INTO hotels(global_identifier, name, description, location, total_price, image_url)
                VALUES
                    (:hotelIdentifier, 'Amazing hotel', 'Eloquent description', 'Barcelona', 150, 'amazing-hotel-image.com')
                """;

        MapSqlParameterSource queryParameters = new MapSqlParameterSource();
        queryParameters.addValue("hotelIdentifier", hotelIdentifier);

        namedParameterJdbcTemplate.update(query, queryParameters);
    }

    private void insertReviewWithHotelIdentifier(UUID hotelIdentifier) {
        String query = """
                INSERT INTO reviews(rating, comment, hotel_id)
                VALUES (5, 'This is an amazing hotel!', :hotelIdentifier);
                """;

        MapSqlParameterSource queryParameters = new MapSqlParameterSource();
        queryParameters.addValue("hotelIdentifier", hotelIdentifier);

        namedParameterJdbcTemplate.update(query, queryParameters);
    }
}
