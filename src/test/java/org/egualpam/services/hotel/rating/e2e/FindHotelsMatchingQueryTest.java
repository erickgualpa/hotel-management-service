package org.egualpam.services.hotel.rating.e2e;

import org.egualpam.services.hotel.rating.AbstractIntegrationTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DirtiesContext
@AutoConfigureMockMvc
public class FindHotelsMatchingQueryTest extends AbstractIntegrationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MockMvc mockMvc;

    @AfterEach
    void tearDown() {
        jdbcTemplate.execute("DELETE FROM hotels;");
    }

    @Test
    @Sql(statements = """
            INSERT INTO hotels(id, name, description, location, total_price, image_url) 
            VALUES ('1', 'Amazing hotel', 'Eloquent description', 'Barcelona', 150, 'amazing-hotel-image.com');
            """)
    void hotelsMatchingQueryShouldBeReturned() throws Exception {

        String request = """
                    {
                        "location": "Barcelona",
                        "priceRange": {
                            "begin": 0,
                            "end": 150
                        }
                    }
                """;

        mockMvc.perform(
                        post("/api/hotel/query")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(request))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
                                [
                                    {
                                      "identifier": "1",
                                      "name": "Amazing hotel",
                                      "description": "Eloquent description",
                                      "location": "Barcelona",
                                      "totalPrice": 150,
                                      "imageURL": "amazing-hotel-image.com"
                                    }
                                ]
                                """
                ));
    }

    @Test
    void emptyListShouldBeReturned_whenNoHotelsMatchQuery() throws Exception {
        String request = """
                    {
                        "location": "%s"
                    }
                """.formatted(UUID.randomUUID().toString());

        mockMvc.perform(
                        post("/api/hotel/query")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(request))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
                                [
                                ]
                                """
                ));
    }
}
