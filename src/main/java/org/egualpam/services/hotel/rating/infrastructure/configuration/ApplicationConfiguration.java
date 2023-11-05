package org.egualpam.services.hotel.rating.infrastructure.configuration;

import org.egualpam.services.hotel.rating.application.hotels.FindHotelsByAverageRating;
import org.egualpam.services.hotel.rating.application.reviews.CreateReview;
import org.egualpam.services.hotel.rating.application.reviews.FindReviewsByHotelIdentifier;
import org.egualpam.services.hotel.rating.domain.hotels.HotelRepository;
import org.egualpam.services.hotel.rating.domain.reviews.ReviewRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public FindHotelsByAverageRating findHotelsByAverageRating(HotelRepository hotelRepository) {
        return new FindHotelsByAverageRating(hotelRepository);
    }

    @Bean
    public FindReviewsByHotelIdentifier findReviewsByHotelIdentifier(ReviewRepository reviewRepository) {
        return new FindReviewsByHotelIdentifier(reviewRepository);
    }

    @Bean
    public CreateReview createReview(ReviewRepository reviewRepository) {
        return new CreateReview(reviewRepository);
    }
}
