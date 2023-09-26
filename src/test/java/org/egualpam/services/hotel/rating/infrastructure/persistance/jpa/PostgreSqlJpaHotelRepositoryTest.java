package org.egualpam.services.hotel.rating.infrastructure.persistance.jpa;

import org.egualpam.services.hotel.rating.AbstractIntegrationTest;
import org.egualpam.services.hotel.rating.application.HotelQuery;
import org.egualpam.services.hotel.rating.domain.Hotel;
import org.egualpam.services.hotel.rating.domain.HotelRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class PostgreSqlJpaHotelRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private HotelRepository testee;

    @AfterEach
    void tearDown() {
        jdbcTemplate.execute("DELETE FROM hotels;");
    }

    @Test
    void givenEmptyQuery_allHotelsShouldBeReturned() {
        jdbcTemplate.update("""
                INSERT INTO hotels(global_identifier, name, description, location, total_price, image_url)
                VALUES
                    (gen_random_uuid(), 'Amazing hotel', 'Eloquent description', 'Barcelona', 1000, 'amazing-hotel-image.com')                
                """
        );
        List<Hotel> result = testee.findHotelsMatchingQuery(HotelQuery.create().build());
        assertThat(result).hasSize(1);
    }

    @Test
    void givenQueryWithLocationFilter_matchingHotelsShouldBeReturned() {

        UUID hotelIdentifier = UUID.randomUUID();

        insertHotelWithIdentifierAndLocation(hotelIdentifier, UUID.randomUUID().toString());
        insertHotelWithIdentifierAndLocation(hotelIdentifier, "Sydney");

        HotelQuery hotelQuery =
                HotelQuery.create()
                        .withLocation("Sydney")
                        .build();

        List<Hotel> result = testee.findHotelsMatchingQuery(hotelQuery);

        assertThat(result).hasSize(1)
                .extracting("identifier")
                .allSatisfy(
                        actualIdentifier -> assertThat(actualIdentifier).isEqualTo(hotelIdentifier.toString())
                );
    }

    @Test
    void givenQueryWithPriceRangeFilter_matchingHotelsShouldBeReturned() {

        insertHotelWithIdentifierAndTotalPrice(UUID.randomUUID(), 50);
        insertHotelWithIdentifierAndTotalPrice(UUID.randomUUID(), 100);
        insertHotelWithIdentifierAndTotalPrice(UUID.randomUUID(), 125);
        insertHotelWithIdentifierAndTotalPrice(UUID.randomUUID(), 150);
        insertHotelWithIdentifierAndTotalPrice(UUID.randomUUID(), 500);

        List<Hotel> result = testee.findHotelsMatchingQuery(HotelQuery.create()
                .withPriceRange(100, 150)
                .build());

        assertThat(result).hasSize(3);
    }

    private void insertHotelWithIdentifierAndLocation(UUID hotelIdentifier, String hotelLocation) {
        String query = """
                INSERT INTO hotels(global_identifier, name, description, location, total_price, image_url)
                VALUES
                    (:globalIdentifier, 'Amazing hotel', 'Eloquent description', :location, 800, 'amazing-hotel-image.com')             
                """;

        MapSqlParameterSource queryParameters = new MapSqlParameterSource();
        queryParameters.addValue("globalIdentifier", hotelIdentifier);
        queryParameters.addValue("location", hotelLocation);

        namedParameterJdbcTemplate.update(
                query,
                queryParameters
        );
    }

    private void insertHotelWithIdentifierAndTotalPrice(UUID hotelIdentifier, Integer totalPrice) {
        String query = """
                INSERT INTO hotels(global_identifier, name, description, location, total_price, image_url)
                VALUES
                    (:globalIdentifier, 'Amazing hotel', 'Eloquent description', 'Barcelona', :totalPrice, 'amazing-hotel-image.com')             
                """;

        MapSqlParameterSource queryParameters = new MapSqlParameterSource();
        queryParameters.addValue("globalIdentifier", hotelIdentifier);
        queryParameters.addValue("totalPrice", totalPrice);

        namedParameterJdbcTemplate.update(
                query,
                queryParameters
        );
    }
}