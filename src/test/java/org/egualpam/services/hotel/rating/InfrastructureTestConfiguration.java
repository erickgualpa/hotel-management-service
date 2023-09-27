package org.egualpam.services.hotel.rating;

import org.egualpam.services.hotel.rating.helpers.HotelTestRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
public class InfrastructureTestConfiguration {

    @Bean
    public HotelTestRepository hotelTestRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        return new HotelTestRepository(namedParameterJdbcTemplate);
    }
}