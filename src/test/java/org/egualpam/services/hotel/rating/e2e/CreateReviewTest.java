package org.egualpam.services.hotel.rating.e2e;

import org.egualpam.services.hotel.rating.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testcontainers.shaded.org.apache.commons.lang3.RandomUtils.nextInt;

@AutoConfigureMockMvc
class CreateReviewTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void reviewShouldBeCreatedGivenHotelIdentifier() throws Exception {
        UUID hotelIdentifier = randomUUID();
        UUID reviewIdentifier = randomUUID();

        mockMvc.perform(
                        post("/v1/reviews/{reviewIdentifier}", reviewIdentifier.toString())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "hotelIdentifier": "%s",
                                            "rating": "%d",
                                            "comment": "%s"
                                        }
                                        """
                                        .formatted(
                                                hotelIdentifier.toString(),
                                                nextInt(1, 5),
                                                randomAlphabetic(10)
                                        )
                                )
                )
                .andExpect(status().isCreated());
    }
}
