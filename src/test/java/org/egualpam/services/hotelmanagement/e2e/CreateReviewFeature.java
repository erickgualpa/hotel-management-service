package org.egualpam.services.hotelmanagement.e2e;

import org.egualpam.services.hotelmanagement.e2e.models.PublicEventResult;
import org.egualpam.services.hotelmanagement.shared.infrastructure.AbstractIntegrationTest;
import org.egualpam.services.hotelmanagement.shared.infrastructure.helpers.HotelTestRepository;
import org.egualpam.services.hotelmanagement.shared.infrastructure.helpers.RabbitMqTestConsumer;
import org.egualpam.services.hotelmanagement.shared.infrastructure.helpers.ReviewTestRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static java.util.UUID.randomUUID;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testcontainers.shaded.org.apache.commons.lang3.RandomUtils.nextInt;

class CreateReviewFeature extends AbstractIntegrationTest {

    private static final String CREATE_REVIEW_REQUEST = """
            {
                "hotelId": "%s",
                "rating": "%d",
                "comment": "%s"
            }
            """;

    @Autowired
    private HotelTestRepository hotelTestRepository;

    @Autowired
    private ReviewTestRepository reviewTestRepository;

    @Autowired
    private RabbitMqTestConsumer rabbitMqTestConsumer;

    @Test
    void reviewShouldBeCreated() throws Exception {
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
                        post("/v1/reviews/{reviewId}", reviewId.toString())
                                .contentType(APPLICATION_JSON)
                                .content(
                                        String.format(
                                                CREATE_REVIEW_REQUEST,
                                                hotelId,
                                                nextInt(1, 5),
                                                randomAlphabetic(10))))
                .andExpect(status().isCreated());

        assertTrue(reviewTestRepository.reviewExists(reviewId));

        await().atMost(10, SECONDS).untilAsserted(() -> {
            PublicEventResult publicEventResult = rabbitMqTestConsumer.consumeFromQueue("hotelmanagement.reviews");
            assertThat(publicEventResult).satisfies(r -> {
                assertThat(r.type()).isEqualTo("hotelmanagement.reviews.created.v1.0");
                assertThat(r.aggregateId()).isEqualTo(reviewId.toString());
                assertNotNull(r.occurredOn());
            });
        });
    }
}