package org.egualpam.services.hotel.rating.infrastructure.controller;

import org.egualpam.services.hotel.rating.application.reviews.CreateReview;
import org.egualpam.services.hotel.rating.application.reviews.CreateReviewCommand;
import org.egualpam.services.hotel.rating.application.reviews.FindReviewsByHotelIdentifier;
import org.egualpam.services.hotel.rating.domain.shared.InvalidIdentifier;
import org.egualpam.services.hotel.rating.domain.shared.InvalidRating;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReviewController.class)
class ReviewControllerShould {

    @MockBean
    private FindReviewsByHotelIdentifier findReviewsByHotelIdentifier;

    @MockBean
    private CreateReview createReview;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void badRequestIsReturned_whenRatingValueIsOutOfAllowedBounds() throws Exception {
        Integer invalidRating = nextInt(6, 10);

        doThrow(InvalidRating.class)
                .when(createReview)
                .execute(any(CreateReviewCommand.class));

        this.mockMvc
                .perform(
                        post("/v1/reviews/{reviewIdentifier}", randomUUID().toString())
                                .contentType(APPLICATION_JSON)
                                .content("""
                                        {
                                            "hotelIdentifier": "%s",
                                            "rating": "%d",
                                            "comment": "%s"
                                        }
                                        """
                                        .formatted(
                                                randomUUID(),
                                                invalidRating,
                                                randomAlphabetic(10)
                                        )
                                )
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void badRequestIsReturned_whenReviewIdentifierHasInvalidFormat() throws Exception {
        String invalidIdentifier = randomAlphanumeric(10);

        doThrow(InvalidIdentifier.class)
                .when(createReview)
                .execute(any(CreateReviewCommand.class));

        this.mockMvc
                .perform(
                        post("/v1/reviews/{reviewIdentifier}", invalidIdentifier)
                                .contentType(APPLICATION_JSON)
                                .content("""
                                        {
                                            "hotelIdentifier": "%s",
                                            "rating": "%d",
                                            "comment": "%s"
                                        }
                                        """
                                        .formatted(
                                                randomUUID(),
                                                nextInt(1, 5),
                                                randomAlphabetic(10)
                                        )
                                )
                )
                .andExpect(status().isBadRequest());
    }
}
