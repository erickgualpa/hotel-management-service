package org.egualpam.services.hotelmanagement.infrastructure.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.egualpam.services.hotelmanagement.application.shared.Command;
import org.egualpam.services.hotelmanagement.application.shared.CommandBus;
import org.egualpam.services.hotelmanagement.application.shared.Query;
import org.egualpam.services.hotelmanagement.application.shared.QueryBus;
import org.egualpam.services.hotelmanagement.domain.reviews.exception.InvalidRating;
import org.egualpam.services.hotelmanagement.domain.reviews.exception.ReviewAlreadyExists;
import org.egualpam.services.hotelmanagement.domain.shared.exception.InvalidIdentifier;
import org.egualpam.services.hotelmanagement.infrastructure.configuration.InfrastructureConfiguration;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReviewController.class)
class ReviewControllerShould {

    private final ObjectMapper objectMapper = new InfrastructureConfiguration().objectMapper();

    @MockBean
    private CommandBus commandBus;

    @MockBean
    private QueryBus queryBus;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void returnBadRequest_whenPostReviewsIsPerformedWithInvalidRating() throws Exception {
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
                .when(commandBus)
                .publish(any(Command.class));

        mockMvc.perform(
                        post("/v1/reviews/{reviewIdentifier}", reviewIdentifier)
                                .contentType(APPLICATION_JSON)
                                .content(request))
                .andExpect(status().isBadRequest());
    }

    @Test
    void returnBadRequest_whenPostReviewsIsPerformedWithInvalidReviewId() throws Exception {
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
                .when(commandBus)
                .publish(any(Command.class));

        mockMvc.perform(
                        post("/v1/reviews/{reviewIdentifier}", invalidReviewIdentifier)
                                .contentType(APPLICATION_JSON)
                                .content(request))
                .andExpect(status().isBadRequest());
    }

    @Test
    void returnBadRequest_whenPutReviewsIsPerformedWithInvalidReviewId() throws Exception {
        String invalidReviewId = randomAlphanumeric(10);
        String comment = randomAlphabetic(10);

        UpdateReviewRequest createReviewRequest = new UpdateReviewRequest(comment);
        String request = objectMapper.writeValueAsString(createReviewRequest);

        doThrow(InvalidIdentifier.class)
                .when(commandBus)
                .publish(any(Command.class));

        mockMvc.perform(
                        put("/v1/reviews/{reviewIdentifier}", invalidReviewId)
                                .contentType(APPLICATION_JSON)
                                .content(request))
                .andExpect(status().isBadRequest());
    }

    @Test
    void returnConflict_whenPostReviewsIsPerformedWithExistingReviewId() throws Exception {
        CreateReviewRequest createReviewRequest = new CreateReviewRequest(
                randomUUID().toString(),
                nextInt(1, 5),
                randomAlphabetic(10)
        );

        String request = objectMapper.writeValueAsString(createReviewRequest);

        doThrow(ReviewAlreadyExists.class)
                .when(commandBus)
                .publish(any(Command.class));

        mockMvc.perform(
                        post("/v1/reviews/{reviewIdentifier}", randomUUID().toString())
                                .contentType(APPLICATION_JSON)
                                .content(request))
                .andExpect(status().isConflict());
    }

    @Test
    void returnInternalError_whenGetReviewsCausesUnknownException() throws Exception {
        String hotelId = randomUUID().toString();

        doThrow(RuntimeException.class)
                .when(queryBus)
                .publish(any(Query.class));

        mockMvc.perform(get("/v1/reviews", hotelId).queryParam("hotelId", hotelId))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void returnInternalError_whenPostReviewsCausesUnknownException() throws Exception {
        CreateReviewRequest createReviewRequest = new CreateReviewRequest(
                randomUUID().toString(),
                nextInt(1, 5),
                randomAlphabetic(10)
        );

        String request = objectMapper.writeValueAsString(createReviewRequest);

        doThrow(RuntimeException.class)
                .when(commandBus)
                .publish(any(Command.class));

        mockMvc.perform(
                        post("/v1/reviews/{reviewIdentifier}", randomUUID().toString())
                                .contentType(APPLICATION_JSON)
                                .content(request))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void returnInternalError_whenPutReviewsCausesUnknownException() throws Exception {
        String comment = randomAlphabetic(10);
        UpdateReviewRequest createReviewRequest = new UpdateReviewRequest(comment);
        String request = objectMapper.writeValueAsString(createReviewRequest);

        doThrow(RuntimeException.class)
                .when(commandBus)
                .publish(any(Command.class));

        mockMvc.perform(
                        put("/v1/reviews/{reviewIdentifier}", randomUUID().toString())
                                .contentType(APPLICATION_JSON)
                                .content(request))
                .andExpect(status().isInternalServerError());
    }
}
