package org.egualpam.services.hotel.rating.infrastructure.controller;

import org.egualpam.services.hotel.rating.application.hotels.Filters;
import org.egualpam.services.hotel.rating.application.hotels.FindHotelsByRatingAverage;
import org.egualpam.services.hotel.rating.domain.hotels.InvalidPriceRange;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HotelController.class)
class HotelControllerShould {

    @MockBean
    private FindHotelsByRatingAverage findHotelsByRatingAverage;

    @Autowired
    private MockMvc mockMvc;

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

        when(findHotelsByRatingAverage.execute(any(Filters.class))).thenThrow(InvalidPriceRange.class);

        this.mockMvc
                .perform(
                        post("/v1/hotels/query")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(request)
                ).andExpect(status().isBadRequest());
    }
}