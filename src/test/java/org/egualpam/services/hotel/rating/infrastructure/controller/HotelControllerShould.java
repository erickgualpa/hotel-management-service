package org.egualpam.services.hotel.rating.infrastructure.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.egualpam.services.hotel.rating.application.hotels.FindHotelsQuery;
import org.egualpam.services.hotel.rating.domain.hotels.exception.PriceRangeValuesSwapped;
import org.egualpam.services.hotel.rating.infrastructure.cqrs.QueryBus;
import org.egualpam.services.hotel.rating.infrastructure.cqrs.QueryFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HotelController.class)
class HotelControllerShould {

    @MockBean
    private QueryFactory hotelQueryFactory;

    @MockBean
    private QueryBus queryBus;

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void returnBadRequest_whenPriceRangeValuesSwappedIsThrown() throws Exception {
        int minPrice = 500;
        int maxPrice = 50;

        QueryHotelRequest query = new QueryHotelRequest(null, new QueryHotelRequest.PriceRange(minPrice, maxPrice));
        String request = objectMapper.writeValueAsString(query);

        FindHotelsQuery mockFindHotelsQuery = mock(FindHotelsQuery.class);
        when(hotelQueryFactory
                .findHotelsQuery(Optional.empty(), Optional.of(minPrice), Optional.of(maxPrice)))
                .thenReturn(mockFindHotelsQuery);
        when(queryBus.publish(mockFindHotelsQuery)).thenThrow(PriceRangeValuesSwapped.class);

        mockMvc.perform(
                        post("/v1/hotels/query")
                                .contentType(APPLICATION_JSON)
                                .content(request)
                )
                .andExpect(status().isBadRequest());
    }
}
