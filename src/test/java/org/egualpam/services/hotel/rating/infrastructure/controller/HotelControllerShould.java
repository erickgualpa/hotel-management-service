package org.egualpam.services.hotel.rating.infrastructure.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.egualpam.services.hotel.rating.application.shared.Query;
import org.egualpam.services.hotel.rating.application.shared.QueryBus;
import org.egualpam.services.hotel.rating.domain.hotels.exception.PriceRangeValuesSwapped;
import org.egualpam.services.hotel.rating.infrastructure.configuration.InfrastructureConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HotelController.class)
class HotelControllerShould {

    @MockBean
    private QueryBus queryBus;

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new InfrastructureConfiguration().objectMapper();

    @Test
    void returnBadRequest_whenPriceRangeValuesSwappedIsThrown() throws Exception {
        int minPrice = 500;
        int maxPrice = 50;

        QueryHotelRequest query = new QueryHotelRequest(null, new QueryHotelRequest.PriceRange(minPrice, maxPrice));
        String request = objectMapper.writeValueAsString(query);

        doThrow(PriceRangeValuesSwapped.class)
                .when(queryBus)
                .publish(any(Query.class));

        mockMvc.perform(
                        post("/v1/hotels/query")
                                .contentType(APPLICATION_JSON)
                                .content(request)
                )
                .andExpect(status().isBadRequest());
    }
}
