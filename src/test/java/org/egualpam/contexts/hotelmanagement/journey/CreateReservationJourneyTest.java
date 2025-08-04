package org.egualpam.contexts.hotelmanagement.journey;

import static java.util.UUID.randomUUID;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.testcontainers.shaded.org.apache.commons.lang3.RandomUtils.nextInt;

import java.util.UUID;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;

public class CreateReservationJourneyTest extends AbstractIntegrationTest {

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

  private static final String CREATE_ROOM_REQUEST =
      """
          {
              "id": "%s",
              "type": "%s",
              "hotelId": "%s"
          }
          """;

  private static final String CREATE_RESERVATION_REQUEST =
      """
          {
              "id": "%s",
              "roomId": "%s",
              "from": "%s",
              "to": "%s"
          }
          """;

  @Test
  void reservationShouldBeCreated() throws Exception {
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

    // Request room creation
    UUID roomId = randomUUID();
    String roomType = "M";
    String createRoomRequest = CREATE_ROOM_REQUEST.formatted(roomId, roomType, hotelId);

    mockMvc
        .perform(post("/v1/rooms").contentType(APPLICATION_JSON).content(createRoomRequest))
        .andExpect(status().isCreated());

    // Request reservation creation
    UUID reservationId = randomUUID();
    String from = "2026-08-01";
    String to = "2026-08-02";

    String request = CREATE_RESERVATION_REQUEST.formatted(reservationId, roomId, from, to);
    mockMvc
        .perform(post("/v1/reservations").contentType(APPLICATION_JSON).content(request))
        .andExpect(status().isCreated());
  }
}
