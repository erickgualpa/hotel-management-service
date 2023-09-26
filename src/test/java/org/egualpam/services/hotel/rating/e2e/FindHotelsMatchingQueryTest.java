package org.egualpam.services.hotel.rating.e2e;

import org.egualpam.services.hotel.rating.AbstractIntegrationTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DirtiesContext
@AutoConfigureMockMvc
public class FindHotelsMatchingQueryTest extends AbstractIntegrationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private MockMvc mockMvc;

    @AfterEach
    void tearDown() {
        jdbcTemplate.execute("DELETE FROM reviews;");
        jdbcTemplate.execute("DELETE FROM hotels;");
    }

    @Test
    void hotelsMatchingQueryShouldBeReturned() throws Exception {

        UUID hotelIdentifier = UUID.randomUUID();

        insertHotelWithIdentifierAndLocationAndTotalPrice(hotelIdentifier, "Barcelona", 150);

        insertReviewWithRatingAndCommentAndHotelIdentifier(
                5,
                "This is an amazing hotel!",
                hotelIdentifier
        );

        insertReviewWithRatingAndCommentAndHotelIdentifier(
                3,
                "This is an average level hotel!",
                hotelIdentifier
        );

        String request = """
                    {
                        "location": "Barcelona",
                        "priceRange": {
                            "begin": 0,
                            "end": 150
                        }
                    }
                """;

        mockMvc.perform(
                        post("/v1/hotels/query")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(request))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
                                [
                                    {
                                      "identifier": "%s",
                                      "name": "Amazing hotel",
                                      "description": "Eloquent description",
                                      "location": "Barcelona",
                                      "totalPrice": 150,
                                      "imageURL": "amazing-hotel-image.com",
                                      "reviews": [
                                        {
                                            "rating": 5,
                                            "comment": "This is an amazing hotel!"
                                        },
                                        {
                                            "rating": 3,
                                            "comment": "This is an average level hotel!"
                                        }
                                      ]
                                    }
                                ]
                                """.formatted(hotelIdentifier.toString())
                ));
    }

    @Test
    void emptyListShouldBeReturned_whenNoHotelsMatchQuery() throws Exception {
        String request = """
                    {
                        "location": "%s"
                    }
                """.formatted(UUID.randomUUID().toString());

        mockMvc.perform(
                        post("/v1/hotels/query")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(request))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
                                [
                                ]
                                """
                ));
    }

    private void insertHotelWithIdentifierAndLocationAndTotalPrice(
            UUID hotelIdentifier, String hotelLocation, Integer totalPrice) {
        String query = """
                INSERT INTO hotels(global_identifier, name, description, location, total_price, image_url)
                VALUES
                    (:globalIdentifier, 'Amazing hotel', 'Eloquent description', :hotelLocation, :totalPrice, 'amazing-hotel-image.com')
                """;

        MapSqlParameterSource queryParameters = new MapSqlParameterSource();
        queryParameters.addValue("globalIdentifier", hotelIdentifier);
        queryParameters.addValue("hotelLocation", hotelLocation);
        queryParameters.addValue("totalPrice", totalPrice);

        namedParameterJdbcTemplate.update(query, queryParameters);
    }

    private void insertReviewWithRatingAndCommentAndHotelIdentifier(
            Integer rating, String comment, UUID hotelIdentifier) {
        String query = """
                INSERT INTO reviews(rating, comment, hotel_id)
                VALUES (:rating, :comment, :hotelIdentifier);
                """;

        MapSqlParameterSource queryParameters = new MapSqlParameterSource();
        queryParameters.addValue("rating", rating);
        queryParameters.addValue("hotelIdentifier", hotelIdentifier);
        queryParameters.addValue("comment", comment);

        namedParameterJdbcTemplate.update(query, queryParameters);
    }
}
