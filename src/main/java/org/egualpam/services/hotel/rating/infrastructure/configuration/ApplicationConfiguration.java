package org.egualpam.services.hotel.rating.infrastructure.configuration;

import org.egualpam.services.hotel.rating.application.FindHotelsByRatingAverage;
import org.egualpam.services.hotel.rating.domain.HotelRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public FindHotelsByRatingAverage findHotelsByRatingAverage(HotelRepository hotelRepository) {
        return new FindHotelsByRatingAverage(hotelRepository);
    }
}
