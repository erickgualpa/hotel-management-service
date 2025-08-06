package org.egualpam.contexts.hotel.customer.e2e;

import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;
import org.egualpam.contexts.hotel.shared.infrastructure.AbstractIntegrationTest;
import org.egualpam.contexts.hotel.shared.infrastructure.helpers.HotelTestRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class FindHotelsMatchingQueryFeature extends AbstractIntegrationTest {

  private static final String QUERY_HOTEL_REQUEST =
      """
                {
                    "location": "%s",
                    "priceRange": {
                        "begin": %d,
                        "end": %d
                    }
                }
            """;

  private static final String QUERY_HOTEL_RESPONSE =
      """
            {
                hotels: [
                    {
                      "identifier": "%s",
                      "name": "%s",
                      "description": "%s",
                      "location": "%s",
                      "price": %d,
                      "imageURL": "%s",
                      "averageRating": %.2f
                    }
                ]
            }
            """;

  @Autowired private HotelTestRepository hotelTestRepository;

  @Test
  void hotelsMatchingQueryShouldBeReturned() throws Exception {
    UUID hotelIdentifier = randomUUID();
    String hotelName = randomAlphabetic(5);
    String hotelDescription = randomAlphabetic(10);
    String hotelLocation = randomAlphabetic(5);
    String imageURL = "www." + randomAlphabetic(5) + ".com";

    int minPrice = 50;
    int maxPrice = 150;
    int price = nextInt(minPrice, maxPrice);
    int rating = nextInt(1, 5);

    hotelTestRepository.insertHotel(
        hotelIdentifier, hotelName, hotelDescription, hotelLocation, price, imageURL);

    hotelTestRepository.insertHotelAverageRating(hotelIdentifier, (double) rating);

    String request = String.format(QUERY_HOTEL_REQUEST, hotelLocation, minPrice, maxPrice);

    mockMvc
        .perform(post("/v1/hotels/query").contentType(APPLICATION_JSON).content(request))
        .andExpect(status().isOk())
        .andExpect(
            content()
                .json(
                    String.format(
                        QUERY_HOTEL_RESPONSE,
                        hotelIdentifier,
                        hotelName,
                        hotelDescription,
                        hotelLocation,
                        price,
                        imageURL,
                        (double) rating)));
  }
}
