package org.egualpam.contexts.hotelmanagement.e2e;

import static java.lang.String.format;
import static java.util.UUID.randomUUID;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.AbstractIntegrationTest;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.helpers.ReservationTestRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class CreateReservationFeature extends AbstractIntegrationTest {

  private static final String CREATE_RESERVATION_REQUEST =
      """
      {
          "id": "%s",
          "roomType": "%s",
          "from": "%s",
          "to": "%s"
      }
      """;

  @Autowired private ReservationTestRepository reservationTestRepository;

  @Test
  void reservationShouldBeCreated() throws Exception {
    UUID reservationId = randomUUID();
    String roomType = "M";
    String from = "2026-08-01";
    String to = "2026-08-02";

    String request = format(CREATE_RESERVATION_REQUEST, reservationId, roomType, from, to);

    mockMvc
        .perform(post("/v1/reservations").contentType(APPLICATION_JSON).content(request))
        .andExpect(status().isCreated());

    // assertTrue(reservationTestRepository.reservationExists(reservationId));

    // TODO: Also check if an event for reservation creation is needed
  }
}
