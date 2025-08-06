package org.egualpam.contexts.hotel.journey;

import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testcontainers.shaded.org.apache.commons.lang3.RandomUtils.nextInt;

import java.util.UUID;
import org.egualpam.contexts.hotel.shared.infrastructure.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;

public class AsyncCreateHotelJourneyTest extends AbstractIntegrationTest {

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

  @Test
  void hotelShouldBeCreateAsynchronously() throws Exception {
    UUID hotelId = randomUUID();

    // Request hotel creation
    String createHotelRequest =
        CREATE_HOTEL_REQUEST.formatted(
            hotelId,
            randomAlphabetic(5),
            randomAlphabetic(10),
            randomAlphabetic(5),
            nextInt(50, 1000),
            "www." + randomAlphabetic(5) + ".com");
    mockMvc
        .perform(post("/v2/hotels").contentType("application/json").content(createHotelRequest))
        .andExpect(status().isAccepted());

    // Request hotel by id
    Thread.sleep(500);
    mockMvc
        .perform(get("/v1/hotels/{hotelId}", hotelId))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.hotel.averageRating", is(0.0)));
  }
}
