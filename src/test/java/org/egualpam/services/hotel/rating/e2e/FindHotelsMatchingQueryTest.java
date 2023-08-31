package org.egualpam.services.hotel.rating.e2e;

import org.egualpam.services.hotel.rating.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DirtiesContext
@AutoConfigureMockMvc
public class FindHotelsMatchingQueryTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    // TODO: Update and split this test into proper test cases
    @Test
    void hotelsMatchingQueryShouldBeReturned() throws Exception {
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
                        .andReturn();
    }
}
