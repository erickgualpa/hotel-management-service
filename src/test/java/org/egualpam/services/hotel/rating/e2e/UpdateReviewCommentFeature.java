package org.egualpam.services.hotel.rating.e2e;

import org.egualpam.services.hotel.rating.AbstractIntegrationTest;
import org.egualpam.services.hotel.rating.helpers.HotelTestRepository;
import org.egualpam.services.hotel.rating.helpers.ReviewTestRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testcontainers.shaded.org.apache.commons.lang3.RandomUtils.nextInt;

class UpdateReviewCommentFeature extends AbstractIntegrationTest {

    private static final String UPDATE_REVIEW_REQUEST = """
            {
                "comment": "%s"
            }
            """;

    @Autowired
    private HotelTestRepository hotelTestRepository;

    @Autowired
    private ReviewTestRepository reviewTestRepository;

    @Test
    void reviewCommentShouldBeUpdatedGivenReviewIdentifier() throws Exception {
        UUID hotelIdentifier = randomUUID();
        UUID reviewIdentifier = randomUUID();
        String newComment = randomAlphabetic(10);

        hotelTestRepository.insertHotel(
                hotelIdentifier,
                randomAlphabetic(5),
                randomAlphabetic(10),
                randomAlphabetic(5),
                nextInt(50, 1000),
                "www." + randomAlphabetic(5) + ".com"
        );

        reviewTestRepository.insertReview(
                reviewIdentifier,
                nextInt(1, 5),
                randomAlphabetic(10),
                hotelIdentifier
        );

        mockMvc.perform(
                        put("/v1/reviews/{reviewIdentifier}", reviewIdentifier.toString())
                                .contentType(APPLICATION_JSON)
                                .content(String.format(UPDATE_REVIEW_REQUEST, newComment)))
                .andExpect(status().isNoContent());

        assertThat(reviewTestRepository.findReview(reviewIdentifier))
                .satisfies(
                        result -> {
                            assertNotNull(result);
                            assertThat(result.comment()).isEqualTo(newComment);
                        }
                );
    }
}
