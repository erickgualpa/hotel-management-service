package org.egualpam.services.hotelmanagement.hotels.infrastructure.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.egualpam.services.hotelmanagement.application.hotels.HotelView;
import org.egualpam.services.hotelmanagement.application.shared.Query;
import org.egualpam.services.hotelmanagement.application.shared.QueryBus;
import org.egualpam.services.hotelmanagement.domain.hotels.exception.PriceRangeValuesSwapped;
import org.egualpam.services.hotelmanagement.domain.shared.exception.InvalidUniqueId;
import org.egualpam.services.hotelmanagement.shared.infrastructure.HotelManagementServiceApplication;
import org.egualpam.services.hotelmanagement.shared.infrastructure.configuration.SharedConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ContextConfiguration(classes = HotelManagementServiceApplication.class)
@WebMvcTest(HotelController.class)
class HotelControllerShould {

    private final ObjectMapper objectMapper = new SharedConfiguration().objectMapper();

    @MockBean
    private QueryBus queryBus;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void returnBadRequest_whenQueryHotelsRequestHasPriceRangeValuesSwapped() throws Exception {
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

    @Test
    void returnBadRequest_whenGetHotelIsPerformedWithInvalidHotelId() throws Exception {
        String hotelId = randomAlphanumeric(5);

        doThrow(InvalidUniqueId.class)
                .when(queryBus)
                .publish(any(Query.class));

        mockMvc.perform(get("/v1/hotels/{hotelId}", hotelId))
                .andExpect(status().isBadRequest());
    }

    @Test
    void returnNotFound_whenGetHotelIsPerformedWithNonMatchingHotelId() throws Exception {
        String hotelId = randomUUID().toString();

        when(queryBus.publish(any(Query.class))).thenReturn(new HotelView(Optional.empty()));

        mockMvc.perform(get("/v1/hotels/{hotelId}", hotelId))
                .andExpect(status().isNotFound());
    }
}
