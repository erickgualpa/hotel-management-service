package org.egualpam.services.hotel.rating;

import org.egualpam.services.hotel.rating.infrastructure.HotelRatingServiceApplication;
import org.egualpam.services.hotel.rating.infrastructure.configuration.InfrastructureTestConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;

@AutoConfigureMockMvc
@ActiveProfiles("integration-test")
@SpringBootTest(
        classes = HotelRatingServiceApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ContextConfiguration(
        initializers = AbstractIntegrationTest.PostgreSqlInitializer.class,
        classes = {InfrastructureTestConfiguration.class}
)
public abstract class AbstractIntegrationTest {

    private static final PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:latest");

    static {
        postgreSQLContainer.start();
    }

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

    @Autowired
    protected MockMvc mockMvc;
}
