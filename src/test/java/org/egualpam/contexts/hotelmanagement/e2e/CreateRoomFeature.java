package org.egualpam.contexts.hotelmanagement.e2e;

import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.AbstractIntegrationTest;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.helpers.HotelTestRepository;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.helpers.RoomTestRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class CreateRoomFeature extends AbstractIntegrationTest {

  private static final String CREATE_ROOM_REQUEST =
      """
      {
          "id": "%s",
          "type": "%s",
          "hotelId": "%s"
      }
      """;

  @Autowired private HotelTestRepository hotelTestRepository;
  @Autowired private RoomTestRepository roomTestRepository;

  @Test
  void roomShouldBeCreated() throws Exception {
    UUID roomId = randomUUID();

    UUID hotelId = randomUUID();
    String hotelName = randomAlphabetic(5);
    String hotelDescription = randomAlphabetic(10);
    String hotelLocation = randomAlphabetic(10);
    Integer hotelPrice = 100;
    String hotelImageURL = "www." + randomAlphabetic(5) + ".com";

    hotelTestRepository.insertHotel(
        hotelId, hotelName, hotelDescription, hotelLocation, hotelPrice, hotelImageURL);

    String request = String.format(CREATE_ROOM_REQUEST, roomId, "S", hotelId);

    mockMvc
        .perform(post("/v1/rooms").contentType(APPLICATION_JSON).content(request))
        .andExpect(status().isCreated());

    assertTrue(roomTestRepository.roomExists(roomId));

    // TODO: Also check if an event for room creation is needed
  }
}
