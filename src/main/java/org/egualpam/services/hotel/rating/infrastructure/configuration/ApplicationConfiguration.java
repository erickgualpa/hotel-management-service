package org.egualpam.services.hotel.rating.infrastructure.configuration;

import org.egualpam.services.hotel.rating.application.FindHotelsByRatingAverage;
import org.egualpam.services.hotel.rating.application.FindReviews;
import org.egualpam.services.hotel.rating.domain.HotelRepository;
import org.egualpam.services.hotel.rating.domain.ReviewRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public FindHotelsByRatingAverage findHotelsByRatingAverage(
            HotelRepository hotelRepository, ReviewRepository reviewRepository) {
        return new FindHotelsByRatingAverage(hotelRepository, reviewRepository);
    }

    @Bean
    public FindReviews findReviews(ReviewRepository reviewRepository) {
        return new FindReviews(reviewRepository);
    }
}
