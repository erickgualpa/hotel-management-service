package org.egualpam.services.hotel.rating.infrastructure.controller;

import org.egualpam.services.hotel.rating.application.hotels.FindHotelsByRatingAverage;
import org.egualpam.services.hotel.rating.domain.hotels.InvalidPriceRange;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.assertj.core.api.Assertions.assertThat;
import static org.egualpam.services.hotel.rating.application.hotels.HotelFilters.LOCATION;
import static org.egualpam.services.hotel.rating.application.hotels.HotelFilters.PRICE_RANGE_BEGIN;
import static org.egualpam.services.hotel.rating.application.hotels.HotelFilters.PRICE_RANGE_END;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HotelController.class)
class HotelControllerTest {

    @MockBean
    private FindHotelsByRatingAverage findHotelsByRatingAverage;

    @Captor
    private ArgumentCaptor<Map<String, String>> filtersCaptor;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void queryIsAcceptedSuccessfully() throws Exception {
        String location = randomAlphabetic(5);
        Integer priceRangeBegin = 100;
        Integer priceRangeEnd = 150;

        String request = """
                    {
                        "location": "%s",
                        "priceRange": {
                            "begin": %d,
                            "end": %d
                        }
                    }
                """.formatted
                (
                        location,
                        priceRangeBegin,
                        priceRangeEnd
                );

        this.mockMvc
                .perform(
                        post("/v1/hotels/query")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(request)
                ).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", is(0)));

        verify(findHotelsByRatingAverage).execute(filtersCaptor.capture());

        assertThat(filtersCaptor.getValue())
                .satisfies(filters -> {
                            assertThat(filters).containsEntry(LOCATION.getValue(), location);
                            assertThat(filters).containsEntry(PRICE_RANGE_BEGIN.getValue(), priceRangeBegin.toString());
                            assertThat(filters).containsEntry(PRICE_RANGE_END.getValue(), priceRangeEnd.toString());
                        }
                );
    }

    @Test
    void badRequestIsReturnedWhenPriceRangeFilterIsInvalid() throws Exception {
        String request = """
                    {
                        "priceRange": {
                            "begin": %d,
                            "end": %d
                        }
                    }
                """.formatted
                (
                        500,
                        50
                );

        when(findHotelsByRatingAverage.execute(anyMap())).thenThrow(InvalidPriceRange.class);

        this.mockMvc
                .perform(
                        post("/v1/hotels/query")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(request)
                ).andExpect(status().isBadRequest());
    }
}
