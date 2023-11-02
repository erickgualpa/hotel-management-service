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

    @Transactional
    public void execute(CreateReviewCommand command) {
        Review review = new Review(
                new Identifier(command.reviewIdentifier()),
                new Identifier(command.hotelIdentifier()),
                new Rating(command.rating()),
                new Comment(command.comment())
        );
        reviewRepository.save(review);
    }
}
