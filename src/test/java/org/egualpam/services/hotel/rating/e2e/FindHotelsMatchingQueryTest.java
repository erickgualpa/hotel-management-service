package org.egualpam.services.hotel.rating.e2e;

import org.egualpam.services.hotel.rating.AbstractIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityManager;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// TODO: Review this test class to see if it could be cleaned up
@DirtiesContext
@AutoConfigureMockMvc
public class FindHotelsMatchingQueryTest extends AbstractIntegrationTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        entityManager.createNativeQuery(
                """
                        INSERT INTO hotels(id, name, description, location, total_price, image_url)
                        VALUES ('1', 'Amazing hotel', 'Eloquent description', 'Barcelona', 150, 'amazing-hotel-image.com');
                        """
        );
    }

    @Test
    void hotelsMatchingQueryShouldBeReturned() throws Exception {
        String request = """
                    {
                        "location": "Barcelona",
                        "priceRange": {
                            "begin": 0,
                            "end": 300
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
