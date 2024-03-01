package org.egualpam.services.hotel.rating.e2e;

import org.egualpam.services.hotel.rating.helpers.HotelTestRepository;
import org.egualpam.services.hotel.rating.helpers.ReviewTestRepository;
import org.egualpam.services.hotel.rating.infrastructure.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FindReviewsMatchingHotelFeature extends AbstractIntegrationTest {

    private static final String FIND_REVIEW_RESPONSE = """
            {
                "reviews": [
                    {
                        "rating": %d,
                        "comment": "%s"
                    }
                ]
            }
            """;

    @Autowired
    private HotelTestRepository hotelTestRepository;

    @Autowired
    private ReviewTestRepository reviewTestRepository;

    @Test
    void reviewsMatchingHotelIdentifierShouldBeReturned() throws Exception {
        UUID hotelIdentifier = randomUUID();
        Integer rating = nextInt(1, 5);
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
                        randomUUID(),
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
                .andExpect(content().json(
                                String.format(
                                        FIND_REVIEW_RESPONSE,
                                        rating,
                                        comment
                                )
                        )
                );
    }
}
