package org.egualpam.contexts.hotelmanagement.e2e;

import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.AbstractIntegrationTest;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.helpers.HotelTestRepository;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.helpers.ReviewTestRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class FindReviewsMatchingHotelFeature extends AbstractIntegrationTest {

  private static final String FIND_REVIEW_RESPONSE =
      """
            {
                "reviews": [
                    {
                        "rating": %d,
                        "comment": "%s"
                    }
                ]
            }
            """;

  @Autowired private HotelTestRepository hotelTestRepository;

  @Autowired private ReviewTestRepository reviewTestRepository;

  @Test
  void reviewsMatchingHotelIdShouldBeReturned() throws Exception {
    UUID hotelId = randomUUID();
    Integer rating = nextInt(1, 5);
    String comment = randomAlphabetic(10);

    hotelTestRepository.insertHotel(
        hotelId,
        randomAlphabetic(5),
        randomAlphabetic(10),
        randomAlphabetic(5),
        nextInt(50, 1000),
        "www." + randomAlphabetic(5) + ".com");

    reviewTestRepository.insertReview(randomUUID(), rating, comment, hotelId);

    mockMvc
        .perform(get("/v1/reviews").queryParam("hotelId", hotelId.toString()))
        .andExpect(status().isOk())
        .andExpect(content().json(String.format(FIND_REVIEW_RESPONSE, rating, comment)));
  }
}
