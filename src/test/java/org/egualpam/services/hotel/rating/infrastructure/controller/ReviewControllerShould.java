package org.egualpam.services.hotel.rating.infrastructure.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.egualpam.services.hotel.rating.application.reviews.CreateReviewCommand;
import org.egualpam.services.hotel.rating.domain.shared.InvalidIdentifier;
import org.egualpam.services.hotel.rating.domain.shared.InvalidRating;
import org.egualpam.services.hotel.rating.infrastructure.cqrs.CommandBus;
import org.egualpam.services.hotel.rating.infrastructure.cqrs.CommandFactory;
import org.egualpam.services.hotel.rating.infrastructure.cqrs.QueryBus;
import org.egualpam.services.hotel.rating.infrastructure.cqrs.QueryFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReviewController.class)
class ReviewControllerShould {

    @MockBean
    private CommandFactory commandFactory;

    @MockBean
    private CommandBus commandBus;

    @MockBean
    private QueryFactory queryFactory;

    @MockBean
    private QueryBus queryBus;

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void returnBadRequest_whenInvalidRatingIsThrown() throws Exception {
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

        CreateReviewCommand mockCreateReviewCommand = mock(CreateReviewCommand.class);
        when(commandFactory
                .createReviewCommand(
                        reviewIdentifier,
                        hotelIdentifier,
                        invalidRating,
                        comment))
                .thenReturn(mockCreateReviewCommand);
        doThrow(InvalidRating.class)
                .when(commandBus)
                .publish(mockCreateReviewCommand);

        mockMvc.perform(
                        post("/v1/reviews/{reviewIdentifier}", reviewIdentifier)
                                .contentType(APPLICATION_JSON)
                                .content(request)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void returnBadRequest_whenInvalidIdentifierIsThrown() throws Exception {
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

        CreateReviewCommand mockCreateReviewCommand = mock(CreateReviewCommand.class);
        when(commandFactory
                .createReviewCommand(
                        invalidReviewIdentifier,
                        invalidHotelIdentifier,
                        rating,
                        comment))
                .thenReturn(mockCreateReviewCommand);
        doThrow(InvalidIdentifier.class)
                .when(commandBus)
                .publish(mockCreateReviewCommand);

        mockMvc.perform(
                        post("/v1/reviews/{reviewIdentifier}", invalidReviewIdentifier)
                                .contentType(APPLICATION_JSON)
                                .content(request)
                )
                .andExpect(status().isBadRequest());
    }
}
