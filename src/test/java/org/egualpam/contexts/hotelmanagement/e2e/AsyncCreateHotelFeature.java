package org.egualpam.contexts.hotelmanagement.e2e;

import static java.util.UUID.randomUUID;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.UUID;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.AbstractIntegrationTest;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.helpers.HotelTestRepository;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.helpers.PublicEventResult;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.helpers.RabbitMqTestConsumer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class AsyncCreateHotelFeature extends AbstractIntegrationTest {

  private static final String CREATE_HOTEL_REQUEST =
      """
            {
                "id": "%s",
                "name": "%s",
                "description": "%s",
                "location": "%s",
                "price": %d,
                "imageURL": "%s"
            }
            """;

  @Autowired private HotelTestRepository hotelTestRepository;
  @Autowired private RabbitMqTestConsumer rabbitMqTestConsumer;

  @Test
  void hotelShouldBeCreated() throws Exception {
    UUID hotelId = randomUUID();
    String hotelName = randomAlphabetic(5);
    String hotelDescription = randomAlphabetic(10);
    String hotelLocation = randomAlphabetic(10);
    Integer hotelPrice = 100;
    String hotelImageURL = "www." + randomAlphabetic(5) + ".com";

    String request =
        String.format(
            CREATE_HOTEL_REQUEST,
            hotelId,
            hotelName,
            hotelDescription,
            hotelLocation,
            hotelPrice,
            hotelImageURL);

    mockMvc
        .perform(post("/v2/hotels").contentType(APPLICATION_JSON).content(request))
        .andExpect(status().isAccepted());

    await()
        .atMost(10, SECONDS)
        .untilAsserted(
            () -> {
              assertTrue(hotelTestRepository.hotelExists(hotelId));
              domainEventIsPublished(hotelId);
            });
  }

  private void domainEventIsPublished(UUID hotelId) throws IOException {
    PublicEventResult publicEventResult =
        rabbitMqTestConsumer.consumeFromQueue("hotelmanagement.hotel");
    assertThat(publicEventResult)
        .satisfies(
            r -> {
              try {
                UUID.fromString(r.id());
              } catch (IllegalArgumentException e) {
                fail("Invalid public event id: [%s]".formatted(r.id()));
              }
              assertThat(r.type()).isEqualTo("hotelmanagement.hotel.created.v1.0");
              assertThat(r.aggregateId()).isEqualTo(hotelId.toString());
              assertNotNull(r.occurredOn());
            });
  }
}
