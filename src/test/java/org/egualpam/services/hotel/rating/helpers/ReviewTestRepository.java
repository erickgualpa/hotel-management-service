package org.egualpam.services.hotel.rating.helpers;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.UUID;

import static java.util.Objects.nonNull;

public class ReviewTestRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public ReviewTestRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public void insertReview(
            Integer rating, String comment, UUID hotelIdentifier) {
        String query = """
                INSERT INTO reviews(rating, comment, hotel_id)
                VALUES (:rating, :comment, :hotelIdentifier);
                """;

        MapSqlParameterSource queryParameters = new MapSqlParameterSource();
        queryParameters.addValue("rating", rating);
        queryParameters.addValue("hotelIdentifier", hotelIdentifier);
        queryParameters.addValue("comment", comment);

        namedParameterJdbcTemplate.update(query, queryParameters);
    }

    public boolean reviewExists(UUID reviewIdentifier) {
        String query = """
                SELECT COUNT(*)
                FROM reviews
                WHERE id = :reviewIdentifier
                """;

        MapSqlParameterSource queryParameters = new MapSqlParameterSource();
        queryParameters.addValue("reviewIdentifier", reviewIdentifier);

        Integer count = namedParameterJdbcTemplate.queryForObject(
                query,
                queryParameters,
                Integer.class
        );

        return nonNull(count) && count == 1;
    }
}
