package org.egualpam.services.hotel.rating.infrastructure.persistance;

import org.egualpam.services.hotel.rating.HotelRatingServiceApplication;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = HotelRatingServiceApplication.class)
@ActiveProfiles("integration-test")
@ContextConfiguration(initializers = PostgreSqlRatedHotelRepositoryTest.PostgreSqlInitializer.class)
public class PostgreSqlRatedHotelRepositoryTest {

    @Autowired
    private RatedHotelRepository testee;

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

    @Test
    void givenAnyQuery_matchingRatedHotelsShouldBeReturned() {

        // TODO: Complete this test case (Query filtering is not implemented yet!)

        List<RatedHotel> result = testee.findHotelsMatchingQuery(HotelQuery.create().build());

        assertThat(result).hasSize(1)
                .allSatisfy(
                        actualHotel ->
                        {
                            assertThat(actualHotel.getIdentifier()).isEqualTo("1");
                            assertThat(actualHotel.getName()).isEqualTo("Amazing hotel");
                            assertThat(actualHotel.getDescription()).isEqualTo("Eloquent description");
                            assertThat(actualHotel.getTotalPrice()).isEqualTo(150);
                            assertThat(actualHotel.getImageURL()).isEqualTo("amazing-hotel-image.com");

                            assertThat(actualHotel.getLocation()).satisfies(
                                    actualHotelLocation -> {
                                        assertNotNull(actualHotelLocation.getIdentifier());
                                        assertThat(actualHotelLocation.getName()).isEqualTo("Barcelona");
                                    });
                        }

                );
    }
}
