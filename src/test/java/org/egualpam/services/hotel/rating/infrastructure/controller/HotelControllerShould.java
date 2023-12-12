package org.egualpam.services.hotel.rating.infrastructure.controller;

import org.egualpam.services.hotel.rating.application.hotels.Filters;
import org.egualpam.services.hotel.rating.application.hotels.FindHotelsByAverageRating;
import org.egualpam.services.hotel.rating.domain.hotels.InvalidPriceRange;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HotelController.class)
class HotelControllerShould {

    @MockBean
    private FindHotelsByAverageRating findHotelsByAverageRating;

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void returnBadRequest_whenPriceRangeFilterIsInvalid() throws Exception {
        QueryHotelRequest query = new QueryHotelRequest(null, new QueryHotelRequest.PriceRange(500, 50));
        String request = objectMapper.writeValueAsString(query);

        when(findHotelsByAverageRating.execute(any(Filters.class))).thenThrow(InvalidPriceRange.class);

        mockMvc.perform(
                        post("/v1/hotels/query")
                                .contentType(APPLICATION_JSON)
                                .content(request)
                )
                .andExpect(status().isBadRequest());
    }
}
