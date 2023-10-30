package org.egualpam.services.hotel.rating.infrastructure.configuration;

import org.egualpam.services.hotel.rating.application.hotels.FindHotelsByRatingAverage;
import org.egualpam.services.hotel.rating.application.reviews.CreateReview;
import org.egualpam.services.hotel.rating.application.reviews.FindReviews;
import org.egualpam.services.hotel.rating.domain.hotels.HotelRepository;
import org.egualpam.services.hotel.rating.domain.reviews.ReviewRepository;
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

    @Bean
    public CreateReview createReview(ReviewRepository reviewRepository) {
        return new CreateReview(reviewRepository);
    }
}
