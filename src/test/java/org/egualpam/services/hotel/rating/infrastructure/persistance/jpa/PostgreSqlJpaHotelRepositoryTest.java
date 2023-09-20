package org.egualpam.services.hotel.rating.infrastructure.persistance.jpa;

import org.egualpam.services.hotel.rating.AbstractIntegrationTest;
import org.egualpam.services.hotel.rating.application.HotelQuery;
import org.egualpam.services.hotel.rating.domain.Hotel;
import org.egualpam.services.hotel.rating.domain.HotelRepository;
import org.junit.jupiter.api.AfterEach;
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
    @Sql(statements = """
            INSERT INTO hotels(id, name, description, location, total_price, image_url)
            VALUES
                ('1', 'Amazing hotel', 'Eloquent description', 'Barcelona', 1000, 'amazing-hotel-image.com'),
                ('2', 'Another hotel', 'Eloquent description', 'Berlin', 400, 'amazing-hotel-image.com'),
                ('3', 'And another hotel', 'Eloquent description', 'Quito', 750, 'amazing-hotel-image.com');
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
            INSERT INTO hotels(id, name, description, location, total_price, image_url)
            VALUES
                ('1', 'Amazing hotel', 'Eloquent description', 'Sydney', 800, 'amazing-hotel-image.com'),
                ('2', 'Another amazing hotel', 'Eloquent description', 'Amsterdam', 100, 'amazing-hotel-image.com');
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
                        hotelIdentifier -> assertThat(hotelIdentifier).isEqualTo("1")
                );
    }

    @ParameterizedTest
    @MethodSource("matchingPriceRangeHotelQueries")
    @Sql(statements = """
            INSERT INTO hotels(id, name, description, location, total_price, image_url)
            VALUES
                ('1', 'Amazing hotel', 'Eloquent description', 'Barcelona', 150, 'amazing-hotel-image.com'),
                ('2', 'Another amazing hotel', 'Eloquent description', 'Amsterdam', 100, 'amazing-hotel-image.com');
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