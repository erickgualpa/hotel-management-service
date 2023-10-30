package org.egualpam.services.hotel.rating.infrastructure.controller;

import org.egualpam.services.hotel.rating.application.reviews.FindReviews;
import org.egualpam.services.hotel.rating.application.reviews.ReviewDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReviewController.class)
class ReviewControllerTest {

    @MockBean
    private FindReviews findReviews;

    @Autowired
    private MockMvc mockMvc;

    // TODO: Review if this test is still needed
    @Test
    void reviewsMatchingHotelIdentifierAreReturnedSuccessfully() throws Exception {

        String hotelIdentifier = randomUUID().toString();

        when(findReviews.findByHotelIdentifier(hotelIdentifier))
                .thenReturn(
                        List.of(
                                new ReviewDto(nextInt(1, 5), randomAlphabetic(5))
                        )
                );

        this.mockMvc
                .perform(
                        get("/v1/reviews").queryParam("hotelIdentifier", hotelIdentifier)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", is(1)));
    }
}
