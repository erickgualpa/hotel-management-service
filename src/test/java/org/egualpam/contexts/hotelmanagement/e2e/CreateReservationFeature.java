package org.egualpam.contexts.hotelmanagement.e2e;

import static java.lang.String.format;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

import java.util.UUID;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.AbstractIntegrationTest;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.helpers.HotelTestRepository;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.helpers.ReservationTestRepository;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.helpers.RoomTestRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class CreateReservationFeature extends AbstractIntegrationTest {

  private static final String CREATE_RESERVATION_REQUEST =
      """
      {
          "id": "%s",
          "roomId": "%s",
          "from": "%s",
          "to": "%s"
      }
      """;

  @Autowired private HotelTestRepository hotelTestRepository;
  @Autowired private RoomTestRepository roomTestRepository;
  @Autowired private ReservationTestRepository reservationTestRepository;

  @Test
  void reservationShouldBeCreated() throws Exception {
    UUID reservationId = randomUUID();
    UUID hotelId = randomUUID();
    UUID roomId = randomUUID();
    String roomType = "M";
    String from = "2026-08-01";
    String to = "2026-08-02";

    String request = format(CREATE_RESERVATION_REQUEST, reservationId, roomId, from, to);

    createHotel(hotelId);
    roomTestRepository.insertRoom(roomId.toString(), roomType, hotelId.toString());

    mockMvc
        .perform(post("/v1/reservations").contentType(APPLICATION_JSON).content(request))
        .andExpect(status().isCreated());

    assertTrue(reservationTestRepository.reservationExists(reservationId));

    // TODO: Also check if an event for reservation creation is needed
  }

  private void createHotel(UUID hotelId) {
    String hotelName = randomAlphabetic(5);
    String hotelDescription = randomAlphabetic(10);
    String hotelLocation = randomAlphabetic(10);
    Integer hotelPrice = 100;
    String hotelImageURL = "www." + randomAlphabetic(5) + ".com";
    hotelTestRepository.insertHotel(
        hotelId, hotelName, hotelDescription, hotelLocation, hotelPrice, hotelImageURL);
  }
}
