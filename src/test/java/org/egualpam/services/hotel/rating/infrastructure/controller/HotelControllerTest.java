package org.egualpam.services.hotel.rating.infrastructure.controller;

import org.egualpam.services.hotel.rating.application.FindHotelsByRatingAverage;
import org.egualpam.services.hotel.rating.application.HotelDto;
import org.egualpam.services.hotel.rating.application.HotelQuery;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static java.time.LocalTime.now;
import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HotelController.class)
class HotelControllerTest {

    @MockBean
    private FindHotelsByRatingAverage findHotelsByRatingAverage;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void queryIsAcceptedSuccessfully() throws Exception {
        String request = """
                    {
                        "location": "%s",
                        "checkIn": "%s",
                        "priceRange": {
                            "begin": %d,
                            "end": %d
                        }
                    }
                """.formatted
                (
                        randomAlphabetic(5),
                        now().toString(),
                        100,
                        150
                );

        this.mockMvc
                .perform(
                        post("/v1/hotels/query")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(request))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", is(0)));
    }

    @Test
    void hotelsMatchingQueryAreReturnedSuccessfully() throws Exception {
        when(findHotelsByRatingAverage.execute(any(HotelQuery.class)))
                .thenReturn(
                        List.of(
                                new HotelDto(
                                        randomUUID().toString(),
                                        randomAlphabetic(5),
                                        randomAlphabetic(10),
                                        randomAlphabetic(5),
                                        nextInt(50, 1000),
                                        "www." + randomAlphanumeric(5) + ".com",
                                        Collections.emptyList())));

        this.mockMvc
                .perform(
                        post("/v1/hotels/query")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{ }"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", is(1)));
    }
}
