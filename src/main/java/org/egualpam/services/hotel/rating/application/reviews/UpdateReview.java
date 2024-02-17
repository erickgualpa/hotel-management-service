package org.egualpam.services.hotel.rating.application.reviews;

import org.egualpam.services.hotel.rating.application.shared.InternalCommand;
import org.egualpam.services.hotel.rating.domain.reviews.Review;
import org.egualpam.services.hotel.rating.domain.reviews.ReviewRepository;
import org.egualpam.services.hotel.rating.domain.shared.Comment;
import org.egualpam.services.hotel.rating.domain.shared.Identifier;

public class UpdateReview implements InternalCommand {

    private final Identifier reviewIdentifier;
    private final Comment comment;
    private final ReviewRepository reviewRepository;

    public UpdateReview(
            String reviewIdentifier,
            String comment,
            ReviewRepository reviewRepository) {
        this.reviewIdentifier = new Identifier(reviewIdentifier);
        this.comment = new Comment(comment);
        this.reviewRepository = reviewRepository;
    }

    @Override
    public void execute() {
        Review review = reviewRepository.findByIdentifier(reviewIdentifier);
        review.updateComment(comment);
        reviewRepository.save(review);
    }
}
