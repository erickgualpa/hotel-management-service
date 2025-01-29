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

import java.util.UUID;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.AbstractIntegrationTest;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.helpers.HotelRatingTestRepository;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.helpers.HotelTestRepository;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.helpers.PublicEventResult;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.helpers.RabbitMqTestConsumer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class CreateHotelFeature extends AbstractIntegrationTest {

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
  @Autowired private HotelRatingTestRepository hotelRatingTestRepository;
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
        .perform(post("/v1/hotels").contentType(APPLICATION_JSON).content(request))
        .andExpect(status().isCreated());

    assertTrue(hotelTestRepository.hotelExists(hotelId));

    // Hotel create event is published
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
                        assertThat(r.type()).isEqualTo("hotelmanagement.hotel.created");
                        assertThat(r.version()).isEqualTo("1.0");
                        assertThat(r.aggregateId()).isEqualTo(hotelId.toString());
                        assertNotNull(r.occurredOn());
                      });
            });

    // TODO: Place this assertions together with the one above
    // TODO: or add something that improves the way event publishing is asserted
    // TODO: Assert hotel rating initialized event is published
    await()
        .atMost(10, SECONDS)
        .untilAsserted(
            () -> {
              assertTrue(hotelRatingTestRepository.hotelRatingIsInitialized(hotelId));

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
                        assertThat(r.type()).isEqualTo("hotelmanagement.hotel-rating.initialized");
                        assertThat(r.version()).isEqualTo("1.0");
                        assertThat(r.aggregateId()).isNotNull();
                        assertNotNull(r.occurredOn());
                      });
            });
  }
}
