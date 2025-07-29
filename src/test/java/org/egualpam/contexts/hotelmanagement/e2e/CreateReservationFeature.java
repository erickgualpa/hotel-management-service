package org.egualpam.contexts.hotelmanagement.e2e;

import static java.util.UUID.randomUUID;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;

class CreateReservationFeature extends AbstractIntegrationTest {

  private static final String CREATE_RESERVATION_REQUEST =
      """
      {
          "id": "%s",
          "roomId": "%s"
      }
      """;

  @Test
  void reservationShouldBeCreated() throws Exception {
    UUID reservationId = randomUUID();
    UUID roomId = randomUUID();

    String request = String.format(CREATE_RESERVATION_REQUEST, reservationId, roomId);

    mockMvc
        .perform(post("/v1/reservations").contentType(APPLICATION_JSON).content(request))
        .andExpect(status().isCreated());

    // assertTrue(roomTestRepository.roomExists(roomId));

    // TODO: Also check if an event for room creation is needed
  }
}
