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
                INSERT INTO hotels(global_identifier, name, description, location, total_price, image_url)
                VALUES
                    (:globalIdentifier, 'Amazing hotel', 'Eloquent description', :hotelLocation, :totalPrice, 'amazing-hotel-image.com')
                """;

        MapSqlParameterSource queryParameters = new MapSqlParameterSource();
        queryParameters.addValue("globalIdentifier", hotelIdentifier);
        queryParameters.addValue("hotelLocation", hotelLocation);
        queryParameters.addValue("totalPrice", totalPrice);

        namedParameterJdbcTemplate.update(query, queryParameters);
    }
}
