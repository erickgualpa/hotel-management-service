package org.egualpam.services.hotel.rating.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import org.egualpam.services.hotel.rating.application.HotelQuery;
import org.egualpam.services.hotel.rating.application.HotelService;
import org.egualpam.services.hotel.rating.domain.Location;
import org.egualpam.services.hotel.rating.domain.RatedHotel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
class HotelControllerTest {

    @MockBean private HotelService hotelService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void queryIsAcceptedSuccessfully() throws Exception {
        String request =
                ""
                        + "{\n"
                        + "    \"location\": \"Barcelona\",\n"
                        + "    \"checkIn\": \"2023-06-24\",\n"
                        + "    \"priceRange\": {\n"
                        + "        \"begin\": 100,\n"
                        + "        \"end\": 200\n"
                        + "    }\n"
                        + "}";

        this.mockMvc
                .perform(
                        post("/api/hotel/query")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(request))
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", is(0)));
    }

    @Test
    void hotelsMatchingQueryAreReturnedSuccessfully() throws Exception {
        when(hotelService.findByQueryAndSortedByRatingAverage(any(HotelQuery.class)))
                .thenReturn(
                        List.of(
                                new RatedHotel(
                                        "some-hotel-identifier",
                                        "some-hotel-name",
                                        "some-hotel-description",
                                        new Location(
                                                "some-location-identifier", "some-location-name"),
                                        250,
                                        "some-hotel-image-url")));

        this.mockMvc
                .perform(
                        post("/api/hotel/query")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{ }"))
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", is(1)));
    }
}
