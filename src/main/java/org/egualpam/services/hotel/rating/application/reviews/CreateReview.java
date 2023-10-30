package org.egualpam.services.hotel.rating.application.reviews;

import org.egualpam.services.hotel.rating.domain.reviews.Comment;
import org.egualpam.services.hotel.rating.domain.reviews.Rating;
import org.egualpam.services.hotel.rating.domain.reviews.Review;
import org.egualpam.services.hotel.rating.domain.reviews.ReviewRepository;
import org.egualpam.services.hotel.rating.domain.shared.Identifier;

public class CreateReview {

    private final ReviewRepository reviewRepository;

    public CreateReview(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public void execute(String reviewIdentifier, Integer rating, String comment) {
        Review review = new Review(
                new Identifier(reviewIdentifier),
                new Rating(rating),
                new Comment(comment)
        );
        reviewRepository.save(review);
    }
}
