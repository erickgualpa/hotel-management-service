package org.egualpam.services.hotel.rating;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
class HotelRatingServiceApplicationTests {

    @Autowired private MockMvc mockMvc;

    @Test
    void serviceShouldReturnMatchingHotelsForGivenQuery() throws Exception {
        String request =
                "{\n"
                        + "    \"location\": \"Barcelona\",\n"
                        + "    \"checkIn\": \"2023-06-24\",\n"
                        + "    \"checkOut\": \"2023-06-28\",\n"
                        + "    \"priceRange\": {\n"
                        + "        \"begin\": 0,\n"
                        + "        \"end\": 100\n"
                        + "    }\n"
                        + "}";

        MvcResult result =
                mockMvc.perform(
                                post("/api/hotel/query")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(request))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        // TODO: This assertions are just included in order to validate a response
                        // that will not come empty, but specific cases should be split into
                        // different test cases
                        .andExpect(jsonPath("$.size()", is(not(0))))
                        .andExpect(jsonPath("$[0][?(@['ratingAverage'])]").isEmpty())
                        .andReturn();
    }
}
