package org.egualpam.services.hotel.rating.e2e;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
public class ReturnHotelsMatchingQueryTest {

    @Autowired
    private MockMvc mockMvc;

    // TODO: Remove and split this test into proper test cases
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
