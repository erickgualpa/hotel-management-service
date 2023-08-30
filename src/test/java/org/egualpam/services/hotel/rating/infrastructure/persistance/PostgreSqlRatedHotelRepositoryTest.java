package org.egualpam.services.hotel.rating.infrastructure.persistance;

import org.egualpam.services.hotel.rating.application.HotelQuery;
import org.egualpam.services.hotel.rating.domain.RatedHotel;
import org.egualpam.services.hotel.rating.domain.RatedHotelRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("integration-test")
@ContextConfiguration(
        initializers = {PostgreSqlRatedHotelRepositoryTest.PostgreSqlInitializer.class},
        classes = PostgreSqlRatedHotelRepositoryTest.PostgreSqlRatedHotelRepositoryTestConfiguration.class
)
public class PostgreSqlRatedHotelRepositoryTest {

    // TODO: Consider if this is the proper test subject for this suite
    @Autowired
    private RatedHotelRepository testee;

    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15-alpine");

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

    // TODO: Remove this configuration if after integrating the actual DB among this service, it is redundant
    @Configuration
    static class PostgreSqlRatedHotelRepositoryTestConfiguration {

        @Bean
        public RatedHotelRepository ratedHotelRepository() {
            return new PostgreSqlRatedHotelRepository();
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

    // TODO: Complete this test case
    @Test
    void givenAnyQuery_matchingRatedHotelsShouldBeReturned() {
        List<RatedHotel> result = testee.findHotelsMatchingQuery(HotelQuery.create().build());
        assertThat(result).hasSize(1);
    }
}
