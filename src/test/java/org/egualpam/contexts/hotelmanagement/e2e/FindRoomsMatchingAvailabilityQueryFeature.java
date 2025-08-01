package org.egualpam.contexts.hotelmanagement.e2e;

import static java.lang.String.format;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;

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

  @Test
  void findRoomsMatchingAvailabilityQuery() throws Exception {
    UUID roomId = UUID.fromString("d6e8ae57-2563-4dfb-ab31-7b6996fd7908");

    mockMvc
        .perform(
            get("/v1/rooms")
                .accept(APPLICATION_JSON)
                .queryParam("availableFrom", "2025-08-01")
                .queryParam("availableTo", "2025-08-15"))
        .andExpect(status().isOk())
        .andExpect(content().json(format(FIND_ROOM_AVAILABILITY_RESPONSE, roomId)));
  }
}
