package org.egualpam.contexts.hotelmanagement.e2e;

import static java.lang.String.format;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.AbstractIntegrationTest;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.helpers.HotelTestRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class UpdateRoomPriceFeature extends AbstractIntegrationTest {

  private static final String UPDATE_ROOM_PRICE_REQUEST =
      """
      {
          "id": "%s",
          "hotelId": "%s",
          "roomType": "%s"
      }
      """;

  @Autowired private HotelTestRepository hotelTestRepository;

  @Test
  void roomPriceShouldBeUpdated() throws Exception {
    UUID roomPriceId = UUID.randomUUID();
    UUID hotelId = UUID.randomUUID();
    String roomType = "M";

    mockMvc
        .perform(
            put("/v1/room-prices")
                .contentType(APPLICATION_JSON)
                .content(format(UPDATE_ROOM_PRICE_REQUEST, roomPriceId, hotelId, roomType)))
        .andExpect(status().isNoContent());
  }
}
