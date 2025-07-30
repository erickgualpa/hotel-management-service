package org.egualpam.contexts.hotelmanagement.e2e;

import static java.lang.String.format;
import static java.util.UUID.randomUUID;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;

class FindHotelRoomAvailabilityFeature extends AbstractIntegrationTest {

  private static final String FIND_ROOM_AVAILABILITY_RESPONSE =
      """
      {
        "results": [
          {
            "day": %d,
            "month": %d,
            "year": %d
          }
        ]
      }
      """;

  // @Autowired private HotelTestRepository hotelTestRepository;

  @Test
  void hotelRoomAvailabilityShouldBeReturned() throws Exception {
    UUID hotelId = randomUUID();
    UUID roomId = randomUUID();

    mockMvc
        .perform(get("/v1/rooms/{roomId}/availability", roomId).accept(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json(format(FIND_ROOM_AVAILABILITY_RESPONSE, 1, 8, 2025)));
  }
}
