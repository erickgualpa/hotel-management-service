package org.egualpam.services.hotel.rating.application.reviews;

import org.egualpam.services.hotel.rating.application.shared.Query;
import org.egualpam.services.hotel.rating.domain.reviews.ReviewRepository;

import java.util.List;

public class ReviewQueryAssistant {
    private final ReviewRepository reviewRepository;

    public ReviewQueryAssistant(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public Query<List<ReviewDto>> findHotelReviews(String hotelIdentifier) {
        return new FindHotelReviewsQuery(hotelIdentifier, reviewRepository);
    }
}
