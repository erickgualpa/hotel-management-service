package org.egualpam.services.hotel.rating.application.reviews;

import org.egualpam.services.hotel.rating.domain.reviews.Review;
import org.egualpam.services.hotel.rating.domain.reviews.ReviewRepository;

public class CreateReview {

    private final ReviewRepository reviewRepository;

    public CreateReview(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public void execute(String reviewIdentifier, Integer rating, String comment) {
        reviewRepository.save(new Review(reviewIdentifier, rating, comment));
    }
}
