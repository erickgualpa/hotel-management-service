package org.egualpam.services.hotel.rating.infrastructure.persistance.jpa;

import org.egualpam.services.hotel.rating.AbstractIntegrationTest;
import org.egualpam.services.hotel.rating.application.HotelQuery;
import org.egualpam.services.hotel.rating.domain.Hotel;
import org.egualpam.services.hotel.rating.domain.HotelRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DirtiesContext
public class PostgreSqlJpaHotelRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private HotelRepository testee;

    public static Stream<Arguments> matchingPriceRangeHotelQueries() {
        return Stream.of(
                Arguments.of(
                        HotelQuery.create()
                                .withPriceRange(100, 100)
                                .build(),
                        "2"),
                Arguments.of(
                        HotelQuery.create()
                                .withPriceRange(101, 150)
                                .build(),
                        "1"),
                Arguments.of(
                        HotelQuery.create()
                                .withPriceRange(150, 150)
                                .build(),
                        "1")
        );
    }

    @AfterEach
    void tearDown() {
        jdbcTemplate.execute("DELETE FROM reviews;");
        jdbcTemplate.execute("DELETE FROM hotels;");
    }

    @Test
    // TODO: Randomize the UUID identifiers used among this queries
    @Sql(statements = """
            INSERT INTO hotels(global_identifier, name, description, location, total_price, image_url)
            VALUES
                ('8d5bea52-8541-421e-80d6-32d4cef1350f', 'Amazing hotel', 'Eloquent description', 'Barcelona', 1000, 'amazing-hotel-image.com'),
                ('879f9e2c-0e0a-4748-a0ee-d10a980187b1', 'Another hotel', 'Eloquent description', 'Berlin', 400, 'amazing-hotel-image.com'),
                ('abf3bfe6-93b9-4416-b1a8-2945f1e46b1e', 'And another hotel', 'Eloquent description', 'Quito', 750, 'amazing-hotel-image.com');
            """)
    void givenEmptyQuery_allHotelsShouldBeReturned() {

        HotelQuery hotelQuery =
                HotelQuery.create()
                        .build();

        List<Hotel> result = testee.findHotelsMatchingQuery(hotelQuery);

        assertThat(result).hasSize(3);
    }

    @Test
    @Sql(statements = """
            INSERT INTO hotels(global_identifier, name, description, location, total_price, image_url)
            VALUES
                ('ad6dba95-5533-4271-b080-952ef8b4c58a', 'Amazing hotel', 'Eloquent description', 'Sydney', 800, 'amazing-hotel-image.com'),
                ('c3819f94-4c42-4ba0-8fff-e4fd9f2048ee', 'Another amazing hotel', 'Eloquent description', 'Amsterdam', 100, 'amazing-hotel-image.com');
            """)
    void givenQueryWithLocationFilter_matchingHotelsShouldBeReturned() {

        HotelQuery hotelQuery =
                HotelQuery.create()
                        .withLocation("Sydney")
                        .build();

        List<Hotel> result = testee.findHotelsMatchingQuery(hotelQuery);

        assertThat(result).hasSize(1)
                .extracting("identifier")
                .allSatisfy(
                        hotelIdentifier -> assertThat(hotelIdentifier).isEqualTo("ad6dba95-5533-4271-b080-952ef8b4c58a")
                );
    }

    @ParameterizedTest
    // TODO: Fix this test
    @Disabled
    @MethodSource("matchingPriceRangeHotelQueries")
    @Sql(statements = """
            INSERT INTO hotels(global_identifier, name, description, location, total_price, image_url)
            VALUES
                ('94737d0a-8644-44ca-ac75-d23f3fcf4616', 'Amazing hotel', 'Eloquent description', 'Barcelona', 150, 'amazing-hotel-image.com'),
                ('5af507f4-13c5-486e-95a3-a8c0e7fed62c', 'Another amazing hotel', 'Eloquent description', 'Amsterdam', 100, 'amazing-hotel-image.com');
            """)
    void givenQueryWithPriceRangeFilter_matchingHotelsShouldBeReturned(
            HotelQuery hotelQuery, String expectedHotelIdentifier) {

        List<Hotel> result = testee.findHotelsMatchingQuery(hotelQuery);

        assertThat(result).hasSize(1)
                .extracting("identifier")
                .allSatisfy(
                        hotelIdentifier -> assertThat(hotelIdentifier).isEqualTo(expectedHotelIdentifier)
                );
    }
}