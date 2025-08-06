package org.egualpam.contexts.hotel.management.e2e;

import static java.lang.String.format;
import static java.util.UUID.randomUUID;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;
import org.egualpam.contexts.AbstractIntegrationTest;
import org.egualpam.contexts.hotel.shared.infrastructure.helpers.RoomPriceTestRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class UpdateRoomPriceFeature extends AbstractIntegrationTest {

  private static final String UPDATE_ROOM_PRICE_REQUEST =
      """
      {
          "hotelId": "%s",
          "roomType": "%s",
          "price": {
            "amount": "%s"
          }
      }
      """;

  @Autowired private RoomPriceTestRepository roomPriceTestRepository;

  @Test
  void roomPriceShouldBeUpdated() throws Exception {
    UUID hotelId = randomUUID();
    String roomType = "M";
    String priceAmount = "100.00";

    mockMvc
        .perform(
            put("/v1/room-prices")
                .contentType(APPLICATION_JSON)
                .content(format(UPDATE_ROOM_PRICE_REQUEST, hotelId, roomType, priceAmount)))
        .andExpect(status().isNoContent());

    // TODO: Enable once repository is implemented
    // assertTrue(roomPriceTestRepository.roomPriceExists(roomPriceId.toString()));
  }
}
