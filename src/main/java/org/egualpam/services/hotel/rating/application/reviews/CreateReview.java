package org.egualpam.services.hotel.rating.application.reviews;

import jakarta.transaction.Transactional;
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

    // TODO: Create class or record for holding this parameters
    @Transactional
    public void execute(String reviewIdentifier, String hotelIdentifier, Integer rating, String comment) {
        Review review = new Review(
                new Identifier(reviewIdentifier),
                new Identifier(hotelIdentifier),
                new Rating(rating),
                new Comment(comment)
        );
        reviewRepository.save(review);
    }
}
