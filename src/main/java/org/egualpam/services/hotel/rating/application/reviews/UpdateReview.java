package org.egualpam.services.hotel.rating.application.reviews;

import org.egualpam.services.hotel.rating.application.shared.InternalCommand;
import org.egualpam.services.hotel.rating.domain.reviews.Review;
import org.egualpam.services.hotel.rating.domain.shared.AggregateId;
import org.egualpam.services.hotel.rating.domain.shared.AggregateRepository;
import org.egualpam.services.hotel.rating.domain.shared.Comment;

public class UpdateReview implements InternalCommand {

    private final AggregateId reviewId;
    private final Comment comment;
    private final AggregateRepository<Review> aggregateReviewRepository;

    public UpdateReview(
            String reviewId,
            String comment,
            AggregateRepository<Review> aggregateReviewRepository) {
        this.reviewId = new AggregateId(reviewId);
        this.comment = new Comment(comment);
        this.aggregateReviewRepository = aggregateReviewRepository;
    }

    @Override
    public void execute() {
        Review review = aggregateReviewRepository.find(reviewId);
        review.updateComment(comment);
        aggregateReviewRepository.save(review);
    }
}
