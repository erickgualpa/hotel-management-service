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

    @Test
    void hotelsMatchingQueryShouldBeReturned() throws Exception {
        String request = """
                    {
                        "location": "Barcelona",
                        "checkIn": "2023-06-24",
                        "checkOut": "2023-06-28",
                        "priceRange": {
                            "begin": 0,
                            "end": 100
                        }
                    }
                """;

        MvcResult result =
                mockMvc.perform(
                                post("/api/hotel/query")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(request))
                        .andExpect(status().isOk())
                        .andReturn();

        // TODO: Complete happy-path test
    }
}
