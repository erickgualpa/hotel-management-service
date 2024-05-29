package org.egualpam.services.hotelmanagement.e2e;

import org.egualpam.services.hotelmanagement.e2e.models.PublicEventResult;
import org.egualpam.services.hotelmanagement.shared.infrastructure.AbstractIntegrationTest;
import org.egualpam.services.hotelmanagement.shared.infrastructure.helpers.EventStoreTestRepository;
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
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testcontainers.shaded.org.apache.commons.lang3.RandomUtils.nextInt;

class UpdateReviewCommentFeature extends AbstractIntegrationTest {

    private static final String UPDATE_REVIEW_REQUEST = """
            {
                "comment": "%s"
            }
            """;

    @Autowired
    private HotelTestRepository hotelTestRepository;

    @Autowired
    private ReviewTestRepository reviewTestRepository;

    @Autowired
    private EventStoreTestRepository eventStoreTestRepository;

    @Autowired
    private RabbitMqTestConsumer rabbitMqTestConsumer;

    @Test
    void reviewCommentShouldBeUpdatedGivenReviewId() throws Exception {
        UUID hotelId = randomUUID();
        UUID reviewId = randomUUID();
        String newComment = randomAlphabetic(10);

        hotelTestRepository.insertHotel(
                hotelId,
                randomAlphabetic(5),
                randomAlphabetic(10),
                randomAlphabetic(5),
                nextInt(50, 1000),
                "www." + randomAlphabetic(5) + ".com"
        );

        reviewTestRepository.insertReview(
                reviewId,
                nextInt(1, 5),
                randomAlphabetic(10),
                hotelId
        );

        mockMvc.perform(put("/v1/reviews/{reviewId}", reviewId.toString())
                        .contentType(APPLICATION_JSON)
                        .content(String.format(UPDATE_REVIEW_REQUEST, newComment)))
                .andExpect(status().isNoContent());

        assertThat(reviewTestRepository.findReview(reviewId))
                .satisfies(
                        result -> {
                            assertNotNull(result);
                            assertThat(result.comment()).isEqualTo(newComment);
                        }
                );

        // Enable the following assertion if 'EventBus' is implemented by 'SimpleEventBus'
        // assertTrue(eventStoreTestRepository.domainEventExists(reviewId, "hotelmanagement.reviews.updated.v1.0"));

        await().atMost(10, SECONDS).untilAsserted(() -> {
            PublicEventResult publicEventResult = rabbitMqTestConsumer.consumeFromQueue("hotelmanagement.reviews");
            assertThat(publicEventResult.type()).isEqualTo("hotelmanagement.reviews.updated.v1.0");
        });
    }
}
