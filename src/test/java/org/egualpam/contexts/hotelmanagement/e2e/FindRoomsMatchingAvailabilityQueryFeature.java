package org.egualpam.contexts.hotelmanagement.e2e;

import static java.util.UUID.randomUUID;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

import java.util.UUID;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.AbstractIntegrationTest;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.helpers.HotelTestRepository;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.helpers.RoomTestRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class FindRoomsMatchingAvailabilityQueryFeature extends AbstractIntegrationTest {

  private static final String FIND_ROOM_AVAILABILITY_RESPONSE =
      """
      {
        "results": [
          {
            "id": "%s"
          }
        ]
      }
      """
          .trim();

  @Autowired private HotelTestRepository hotelTestRepository;
  @Autowired private RoomTestRepository roomTestRepository;

  @Test
  void findRoomsMatchingAvailabilityQuery() throws Exception {
    UUID hotelId = randomUUID();

    UUID roomId = randomUUID();
    String roomType = "S";

    createHotel(hotelId);
    roomTestRepository.insertRoom(roomId.toString(), roomType, hotelId.toString());

    mockMvc
        .perform(
            get("/v1/rooms")
                .accept(APPLICATION_JSON)
                .queryParam("availableFrom", "2025-08-01")
                .queryParam("availableTo", "2025-08-15"))
        .andExpect(status().isOk()) /*
        .andExpect(content().json(format(FIND_ROOM_AVAILABILITY_RESPONSE, roomId)))*/; // TODO: Enable once avoided overlapping between tests
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
