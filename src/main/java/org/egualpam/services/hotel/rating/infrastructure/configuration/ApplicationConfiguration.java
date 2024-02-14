package org.egualpam.services.hotel.rating.infrastructure.configuration;

import org.egualpam.services.hotel.rating.application.hotels.HotelQueryAssistant;
import org.egualpam.services.hotel.rating.application.reviews.CreateReview;
import org.egualpam.services.hotel.rating.application.reviews.ReviewQueryAssistant;
import org.egualpam.services.hotel.rating.domain.hotels.HotelRepository;
import org.egualpam.services.hotel.rating.domain.reviews.ReviewRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public HotelQueryAssistant hotelQueryFactory(HotelRepository hotelRepository) {
        return new HotelQueryAssistant(hotelRepository);
    }

    @Bean
    public ReviewQueryAssistant reviewQueryAssistant(ReviewRepository reviewRepository) {
        return new ReviewQueryAssistant(reviewRepository);
    }

    @Bean
    public CreateReview createReview(ReviewRepository reviewRepository) {
        return new CreateReview(reviewRepository);
    }
}
