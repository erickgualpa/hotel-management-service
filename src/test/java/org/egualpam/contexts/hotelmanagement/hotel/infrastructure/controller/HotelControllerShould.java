package org.egualpam.contexts.hotelmanagement.hotel.infrastructure.controller;

import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import org.egualpam.contexts.hotelmanagement.hotel.application.query.OneHotel;
import org.egualpam.contexts.hotelmanagement.hotel.domain.PriceRangeValuesSwapped;
import org.egualpam.contexts.hotelmanagement.shared.application.query.Query;
import org.egualpam.contexts.hotelmanagement.shared.application.query.QueryBus;
import org.egualpam.contexts.hotelmanagement.shared.domain.InvalidUniqueId;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.HotelManagementServiceApplication;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.configuration.ObjectMapperConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

@ContextConfiguration(classes = HotelManagementServiceApplication.class)
@WebMvcTest(controllers = {GetHotelController.class, QueryHotelController.class})
class HotelControllerShould {

  private final ObjectMapper objectMapper = new ObjectMapperConfiguration().objectMapper();

  @MockBean private QueryBus queryBus;

  @Autowired private MockMvc mockMvc;

  @Test
  void returnBadRequest_whenQueryHotelsRequestHasPriceRangeValuesSwapped() throws Exception {
    int minPrice = 500;
    int maxPrice = 50;

    QueryHotelRequest query =
        new QueryHotelRequest(null, new QueryHotelRequest.PriceRange(minPrice, maxPrice));
    String request = objectMapper.writeValueAsString(query);

    doThrow(PriceRangeValuesSwapped.class).when(queryBus).publish(any(Query.class));

    mockMvc
        .perform(post("/v1/hotels/query").contentType(APPLICATION_JSON).content(request))
        .andExpect(status().isBadRequest());
  }

  @Test
  void returnBadRequest_whenGetHotelIsPerformedWithInvalidHotelId() throws Exception {
    String hotelId = randomAlphanumeric(5);

    doThrow(InvalidUniqueId.class).when(queryBus).publish(any(Query.class));

    mockMvc.perform(get("/v1/hotels/{hotelId}", hotelId)).andExpect(status().isBadRequest());
  }

  @Test
  void returnNotFound_whenGetHotelIsPerformedWithNonMatchingHotelId() throws Exception {
    String hotelId = randomUUID().toString();

    when(queryBus.publish(any(Query.class))).thenReturn(new OneHotel(Optional.empty()));

    mockMvc.perform(get("/v1/hotels/{hotelId}", hotelId)).andExpect(status().isNotFound());
  }
}
