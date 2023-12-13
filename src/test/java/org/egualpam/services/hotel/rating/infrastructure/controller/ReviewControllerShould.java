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
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

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

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldReturnBadRequest_whenInvalidRatingIsThrown() throws Exception {
        String hotelIdentifier = randomUUID().toString();
        String reviewIdentifier = randomUUID().toString();
        String comment = randomAlphabetic(10);
        Integer invalidRating = nextInt(6, 10);

        CreateReviewRequest createReviewRequest = new CreateReviewRequest(
                hotelIdentifier,
                invalidRating,
                comment
        );

        String request = objectMapper.writeValueAsString(createReviewRequest);

        doThrow(InvalidRating.class)
                .when(createReview)
                .execute(any(CreateReviewCommand.class));

        mockMvc.perform(
                        post("/v1/reviews/{reviewIdentifier}", reviewIdentifier)
                                .contentType(APPLICATION_JSON)
                                .content(request)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequest_whenInvalidIdentifierIsThrown() throws Exception {
        String invalidHotelIdentifier = randomAlphanumeric(10);
        String invalidReviewIdentifier = randomAlphanumeric(10);
        String comment = randomAlphabetic(10);
        Integer rating = nextInt(1, 5);

        CreateReviewRequest createReviewRequest = new CreateReviewRequest(
                invalidHotelIdentifier,
                rating,
                comment
        );

        String request = objectMapper.writeValueAsString(createReviewRequest);

        doThrow(InvalidIdentifier.class)
                .when(createReview)
                .execute(any(CreateReviewCommand.class));

        mockMvc.perform(
                        post("/v1/reviews/{reviewIdentifier}", invalidReviewIdentifier)
                                .contentType(APPLICATION_JSON)
                                .content(request)
                )
                .andExpect(status().isBadRequest());
    }
}
