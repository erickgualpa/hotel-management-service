package org.egualpam.services.hotel.rating.e2e;

import org.egualpam.services.hotel.rating.AbstractIntegrationTest;
import org.egualpam.services.hotel.rating.helpers.HotelTestRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testcontainers.shaded.org.apache.commons.lang3.RandomUtils.nextInt;

@AutoConfigureMockMvc
class CreateReviewFeature extends AbstractIntegrationTest {

    private static final String CREATE_REVIEW_REQUEST = """
            {
                "hotelIdentifier": "%s",
                "rating": "%d",
                "comment": "%s"
            }
            """;

    @Autowired
    private HotelTestRepository hotelTestRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void reviewShouldBeCreatedGivenHotelIdentifier() throws Exception {
        UUID hotelIdentifier = randomUUID();
        String reviewIdentifier = randomUUID().toString();

        hotelTestRepository
                .insertHotel(
                        hotelIdentifier,
                        randomAlphabetic(5),
                        randomAlphabetic(10),
                        randomAlphabetic(5),
                        nextInt(50, 1000),
                        "www." + randomAlphabetic(5) + ".com"
                );

        mockMvc.perform(
                        post("/v1/reviews/{reviewIdentifier}", reviewIdentifier)
                                .contentType(APPLICATION_JSON)
                                .content(
                                        String.format(
                                                CREATE_REVIEW_REQUEST,
                                                hotelIdentifier,
                                                nextInt(1, 5),
                                                randomAlphabetic(10)
                                        )
                                )
                )
                .andExpect(status().isCreated());
    }
}
