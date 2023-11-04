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
class FindHotelsMatchingQueryFeature extends AbstractIntegrationTest {

    @Autowired
    private HotelTestRepository hotelTestRepository;

    @Autowired
    private ReviewTestRepository reviewTestRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void hotelsMatchingQueryShouldBeReturned() throws Exception {
        UUID hotelIdentifier = randomUUID();
        String hotelName = randomAlphabetic(5);
        String hotelDescription = randomAlphabetic(10);
        String hotelLocation = randomAlphabetic(5);
        String comment = randomAlphabetic(10);
        String imageURL = "www." + randomAlphabetic(5) + ".com";

        int minPrice = 50;
        int maxPrice = 150;
        int totalPrice = nextInt(minPrice, maxPrice);
        int rating = nextInt(1, 5);

        hotelTestRepository
                .insertHotel(
                        hotelIdentifier,
                        hotelName,
                        hotelDescription,
                        hotelLocation,
                        totalPrice,
                        imageURL
                );

        reviewTestRepository
                .insertReview(
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
                                              "name": "%s",
                                              "description": "%s",
                                              "location": "%s",
                                              "totalPrice": %d,
                                              "imageURL": "%s",
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
                                                hotelName,
                                                hotelDescription,
                                                hotelLocation,
                                                totalPrice,
                                                imageURL,
                                                rating,
                                                comment
                                        )
                        )
                );
    }
}