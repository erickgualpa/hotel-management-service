package org.egualpam.contexts.hotel.customer.e2e;

import static java.util.UUID.randomUUID;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testcontainers.shaded.org.apache.commons.lang3.RandomUtils.nextInt;

import java.util.UUID;
import org.egualpam.contexts.hotel.shared.infrastructure.AbstractIntegrationTest;
import org.egualpam.contexts.hotel.shared.infrastructure.helpers.HotelTestRepository;
import org.egualpam.contexts.hotel.shared.infrastructure.helpers.PublicEventResult;
import org.egualpam.contexts.hotel.shared.infrastructure.helpers.RabbitMqTestConsumer;
import org.egualpam.contexts.hotel.shared.infrastructure.helpers.ReviewTestRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class UpdateReviewCommentFeature extends AbstractIntegrationTest {

  private static final String UPDATE_REVIEW_REQUEST =
      """
            {
                "comment": "%s"
            }
            """;

  @Autowired private HotelTestRepository hotelTestRepository;

  @Autowired private ReviewTestRepository reviewTestRepository;

  @Autowired private RabbitMqTestConsumer rabbitMqTestConsumer;

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
        "www." + randomAlphabetic(5) + ".com");

    reviewTestRepository.insertReview(reviewId, nextInt(1, 5), randomAlphabetic(10), hotelId);

    mockMvc
        .perform(
            put("/v1/reviews/{reviewId}", reviewId.toString())
                .contentType(APPLICATION_JSON)
                .content(String.format(UPDATE_REVIEW_REQUEST, newComment)))
        .andExpect(status().isNoContent());

    assertThat(reviewTestRepository.findReview(reviewId))
        .satisfies(
            result -> {
              assertNotNull(result);
              assertThat(result.comment()).isEqualTo(newComment);
            });

    // TODO: Check if this could be asserted in some other way
    await()
        .atMost(10, SECONDS)
        .untilAsserted(
            () -> {
              PublicEventResult publicEventResult =
                  rabbitMqTestConsumer.consumeFromQueue("hotelmanagement.test");
              assertThat(publicEventResult)
                  .satisfies(
                      r -> {
                        try {
                          UUID.fromString(r.id());
                        } catch (IllegalArgumentException e) {
                          fail("Invalid public event id: [%s]".formatted(r.id()));
                        }
                        assertThat(r.type()).isEqualTo("hotelmanagement.review.updated");
                        assertThat(r.version()).isEqualTo("1.0");
                        assertThat(r.aggregateId()).isEqualTo(reviewId.toString());
                        assertNotNull(r.occurredOn());
                      });
            });
  }
}
