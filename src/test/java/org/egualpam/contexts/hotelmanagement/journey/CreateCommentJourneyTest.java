package org.egualpam.contexts.hotelmanagement.journey;

import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testcontainers.shaded.org.apache.commons.lang3.RandomUtils.nextInt;

import java.util.UUID;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;

public class CreateCommentJourneyTest extends AbstractIntegrationTest {

  @Test
  void hotelAverageRatingShouldBeCreatedWhenReviewCreated() throws Exception {
    UUID hotelId = randomUUID();

    // Create hotel
    String createHotelRequest =
        """
        {
            "id": "%s",
            "name": "%s",
            "description": "%s",
            "location": "%s",
            "price": %d,
            "imageURL": "%s"
        }
        """
            .formatted(
                hotelId,
                randomAlphabetic(5),
                randomAlphabetic(10),
                randomAlphabetic(5),
                nextInt(50, 1000),
                "www." + randomAlphabetic(5) + ".com");
    mockMvc
        .perform(post("/v1/hotels").contentType("application/json").content(createHotelRequest))
        .andExpect(status().isCreated());

    // Verify hotel average rating is zero
    mockMvc
        .perform(get("/v1/hotels/{hotelId}", hotelId))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.hotel.averageRating", is(0.0)));

    // Create hotel review
    UUID reviewId = randomUUID();
    int rating = nextInt(1, 5);
    String comment = randomAlphabetic(10);

    String createReviewRequest =
        """
            {
                "hotelId": "%s",
                "rating": "%d",
                "comment": "%s"
            }
            """
            .formatted(hotelId, rating, comment);

    mockMvc
        .perform(
            post("/v1/reviews/{reviewId}", reviewId.toString())
                .contentType(APPLICATION_JSON)
                .content(createReviewRequest))
        .andExpect(status().isCreated());

    // Get hotel with average rating updated
    Double expected = (double) rating;
    mockMvc
        .perform(get("/v1/hotels/{hotelId}", hotelId))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.hotel.averageRating", is(expected)));
  }
}
