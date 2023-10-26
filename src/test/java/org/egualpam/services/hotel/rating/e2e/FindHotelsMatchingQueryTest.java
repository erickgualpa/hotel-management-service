package org.egualpam.services.hotel.rating.e2e;

import org.egualpam.services.hotel.rating.AbstractIntegrationTest;
import org.egualpam.services.hotel.rating.helpers.HotelTestRepository;
import org.egualpam.services.hotel.rating.helpers.ReviewTestRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class FindHotelsMatchingQueryTest extends AbstractIntegrationTest {

    @Autowired
    private HotelTestRepository hotelTestRepository;

    @Autowired
    private ReviewTestRepository reviewTestRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void hotelsMatchingQueryShouldBeReturned() throws Exception {

        UUID hotelIdentifier = randomUUID();
        String hotelLocation = randomAlphabetic(5);
        String comment = randomAlphabetic(10);

        int minPrice = 50;
        int maxPrice = 150;
        int totalPrice = nextInt(minPrice, maxPrice);
        int rating = nextInt(1, 5);

        hotelTestRepository
                .insertHotelWithIdentifierAndLocationAndTotalPrice(
                        hotelIdentifier,
                        hotelLocation,
                        totalPrice);

        reviewTestRepository.insertReviewWithRatingAndCommentAndHotelIdentifier(
                rating,
                comment,
                hotelIdentifier
        );

        String request = """
                    {
                        "location": "%s",
                        "priceRange": {
                            "begin": %d,
                            "end": %d
                        }
                    }
                """.formatted
                (
                        hotelLocation,
                        minPrice,
                        maxPrice
                );

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
                                      "location": "%s",
                                      "totalPrice": %d,
                                      "imageURL": "amazing-hotel-image.com",
                                      "reviews": [
                                        {
                                            "rating": %d,
                                            "comment": "%s"
                                        }
                                      ]
                                    }
                                ]
                                """.formatted
                                (
                                        hotelIdentifier.toString(),
                                        hotelLocation,
                                        totalPrice,
                                        rating,
                                        comment
                                )
                ));
    }

    @Test
    void emptyListShouldBeReturned_whenNoHotelsMatchQuery() throws Exception {

        String hotelLocation = randomAlphabetic(5);

        int minPrice = 50;
        int maxPrice = 150;
        int totalPrice = nextInt(minPrice, maxPrice);

        String request = """
                    {
                        "location": "%s"
                    }
                """.formatted
                (
                        randomAlphabetic(5)
                );

        hotelTestRepository
                .insertHotelWithIdentifierAndLocationAndTotalPrice(
                        randomUUID(),
                        hotelLocation,
                        totalPrice);

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
