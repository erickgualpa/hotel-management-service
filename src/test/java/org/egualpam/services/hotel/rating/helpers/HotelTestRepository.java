package org.egualpam.services.hotel.rating.helpers;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.UUID;

public final class HotelTestRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public HotelTestRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public void insertHotelWithIdentifierAndLocationAndTotalPrice(
            UUID hotelIdentifier, String hotelLocation, Integer totalPrice) {
        String query = """
                INSERT INTO hotels(id, name, description, location, total_price, image_url)
                VALUES
                    (:hotelIdentifier, 'Amazing hotel', 'Eloquent description', :hotelLocation, :totalPrice, 'amazing-hotel-image.com')
                """;

        MapSqlParameterSource queryParameters = new MapSqlParameterSource();
        queryParameters.addValue("hotelIdentifier", hotelIdentifier);
        queryParameters.addValue("hotelLocation", hotelLocation);
        queryParameters.addValue("totalPrice", totalPrice);

        namedParameterJdbcTemplate.update(query, queryParameters);
    }
}
