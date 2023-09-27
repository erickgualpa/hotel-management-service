package org.egualpam.services.hotel.rating.e2e;

import org.egualpam.services.hotel.rating.AbstractIntegrationTest;
import org.egualpam.services.hotel.rating.helpers.HotelTestRepository;
import org.egualpam.services.hotel.rating.helpers.ReviewTestRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
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
    private HotelTestRepository hotelTestRepository;

    @Autowired
    private ReviewTestRepository reviewTestRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void hotelsMatchingQueryShouldBeReturned() throws Exception {

        UUID hotelIdentifier = UUID.randomUUID();

        hotelTestRepository
                .insertHotelWithIdentifierAndLocationAndTotalPrice(
                        hotelIdentifier,
                        "Barcelona",
                        150);

        reviewTestRepository.insertReviewWithRatingAndCommentAndHotelIdentifier(
                5,
                "This is an amazing hotel!",
                hotelIdentifier
        );

        reviewTestRepository.insertReviewWithRatingAndCommentAndHotelIdentifier(
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
}
