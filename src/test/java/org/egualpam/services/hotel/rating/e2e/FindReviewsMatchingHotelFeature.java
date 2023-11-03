package org.egualpam.services.hotel.rating.e2e;

import org.egualpam.services.hotel.rating.AbstractIntegrationTest;
import org.egualpam.services.hotel.rating.helpers.HotelTestRepository;
import org.egualpam.services.hotel.rating.helpers.ReviewTestRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class FindReviewsMatchingHotelFeature extends AbstractIntegrationTest {

    @Autowired
    private HotelTestRepository hotelTestRepository;

    @Autowired
    private ReviewTestRepository reviewTestRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void reviewsMatchingHotelIdentifierShouldBeReturned() throws Exception {
        UUID hotelIdentifier = randomUUID();

        int rating = nextInt(1, 5);
        String comment = randomAlphabetic(10);

        hotelTestRepository
                .insertHotel(
                        hotelIdentifier,
                        randomAlphabetic(5),
                        randomAlphabetic(10),
                        randomAlphabetic(5),
                        nextInt(50, 1000),
                        "www." + randomAlphabetic(5) + ".com"
                );

        reviewTestRepository
                .insertReview(
                        rating,
                        comment,
                        hotelIdentifier
                );

        mockMvc.perform(
                        get("/v1/reviews")
                                .queryParam(
                                        "hotelIdentifier", hotelIdentifier.toString()
                                )
                )
                .andExpect(status().isOk())
                .andExpect(content().json("""
                                [
                                    {
                                        "rating": %d,
                                        "comment": "%s"
                                    }
                                ]
                                """.formatted
                                (
                                        rating,
                                        comment
                                )
                        )
                );
    }
}
