package org.egualpam.services.hotel.rating.e2e;

import org.egualpam.services.hotel.rating.AbstractIntegrationTest;
import org.egualpam.services.hotel.rating.helpers.EventStoreTestRepository;
import org.egualpam.services.hotel.rating.helpers.HotelTestRepository;
import org.egualpam.services.hotel.rating.helpers.ReviewTestRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testcontainers.shaded.org.apache.commons.lang3.RandomUtils.nextInt;

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
    private ReviewTestRepository reviewTestRepository;

    @Autowired
    private EventStoreTestRepository eventStoreTestRepository;

    @Test
    void reviewShouldBeCreatedGivenHotelId() throws Exception {
        UUID hotelId = randomUUID();
        UUID reviewId = randomUUID();

        hotelTestRepository
                .insertHotel(
                        hotelId,
                        randomAlphabetic(5),
                        randomAlphabetic(10),
                        randomAlphabetic(5),
                        nextInt(50, 1000),
                        "www." + randomAlphabetic(5) + ".com"
                );

        mockMvc.perform(
                        post("/v1/reviews/{reviewIdentifier}", reviewId.toString())
                                .contentType(APPLICATION_JSON)
                                .content(
                                        String.format(
                                                CREATE_REVIEW_REQUEST,
                                                hotelId,
                                                nextInt(1, 5),
                                                randomAlphabetic(10)
                                        )
                                )
                )
                .andExpect(status().isCreated());

        assertTrue(reviewTestRepository.reviewExists(reviewId));
        assertTrue(eventStoreTestRepository.domainEventExists(reviewId, "domain.review.created.v1.0"));
    }
}
