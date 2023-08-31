package org.egualpam.services.hotel.rating.e2e;

import org.egualpam.services.hotel.rating.HotelRatingServiceApplication;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        classes = HotelRatingServiceApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("integration-test")
@ContextConfiguration(initializers = ReturnHotelsMatchingQueryTest.PostgreSqlInitializer.class)
public class ReturnHotelsMatchingQueryTest {

    @Autowired
    private MockMvc mockMvc;

    public static PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:15-alpine");

    static class PostgreSqlInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
                    "spring.datasource.username= " + postgreSQLContainer.getUsername(),
                    "spring.datasource.password=" + postgreSQLContainer.getPassword(),
                    "spring.datasource.driver-class-name=" + postgreSQLContainer.getDriverClassName()
            ).applyTo(applicationContext.getEnvironment());
        }
    }

    @BeforeAll
    static void beforeAll() {
        postgreSQLContainer.start();
    }

    @AfterAll
    static void afterAll() {
        postgreSQLContainer.stop();
    }

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
