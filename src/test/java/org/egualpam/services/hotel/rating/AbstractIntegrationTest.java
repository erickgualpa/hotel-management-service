package org.egualpam.services.hotel.rating;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;

@SpringBootTest(
        classes = HotelRatingServiceApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration-test")
@ContextConfiguration(initializers = AbstractIntegrationTest.PostgreSqlInitializer.class)
public abstract class AbstractIntegrationTest {

    private static final PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:latest");

    static class PostgreSqlInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        /* TODO: This seems to be out-of-sync with the 'postgreSQLContainer' start/stop
         *           - until this is addressed, all the integration test will be tagged with @DirtiesContext annotation
         * */
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
}
