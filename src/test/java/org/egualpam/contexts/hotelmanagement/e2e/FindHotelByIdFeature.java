package org.egualpam.contexts.hotelmanagement.e2e;

import org.egualpam.contexts.hotelmanagement.shared.infrastructure.AbstractIntegrationTest;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.helpers.HotelTestRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FindHotelByIdFeature extends AbstractIntegrationTest {

    private static final String QUERY_HOTEL_RESPONSE = """
             {
                hotel: {
                  "identifier": "%s",
                  "name": "%s",
                  "description": "%s",
                  "location": "%s",
                  "totalPrice": %d,
                  "imageURL": "%s",
                  "averageRating": %.2f
                }
            }
            """;

    @Autowired
    private HotelTestRepository hotelTestRepository;

    @Test
    void hotelMatchingIdShouldBeReturned() throws Exception {
        UUID hotelId = randomUUID();
        String hotelName = randomAlphabetic(5);
        String hotelDescription = randomAlphabetic(10);
        String hotelLocation = randomAlphabetic(5);
        String imageURL = "www." + randomAlphabetic(5) + ".com";

        int minPrice = 50;
        int maxPrice = 150;
        int price = nextInt(minPrice, maxPrice);
        double rating = 0.0;

        hotelTestRepository
                .insertHotel(
                        hotelId,
                        hotelName,
                        hotelDescription,
                        hotelLocation,
                        price,
                        imageURL
                );

        mockMvc.perform(get("/v1/hotels/{hotelId}", hotelId))
                .andExpect(status().isOk())
                .andExpect(content().json(
                                String.format(
                                        QUERY_HOTEL_RESPONSE,
                                        hotelId,
                                        hotelName,
                                        hotelDescription,
                                        hotelLocation,
                                        price,
                                        imageURL,
                                        rating
                                )
                        )
                );
    }
}
