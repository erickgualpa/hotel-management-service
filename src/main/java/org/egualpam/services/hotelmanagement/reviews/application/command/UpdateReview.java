package org.egualpam.services.hotelmanagement.reviews.application.command;

import org.egualpam.services.hotelmanagement.reviews.domain.Comment;
import org.egualpam.services.hotelmanagement.reviews.domain.Review;
import org.egualpam.services.hotelmanagement.shared.application.command.InternalCommand;
import org.egualpam.services.hotelmanagement.shared.domain.AggregateId;
import org.egualpam.services.hotelmanagement.shared.domain.AggregateRepository;
import org.egualpam.services.hotelmanagement.shared.domain.EventBus;

public class UpdateReview implements InternalCommand {

    private final AggregateId reviewId;
    private final Comment comment;
    private final AggregateRepository<Review> reviewRepository;
    private final EventBus eventBus;

    public UpdateReview(
            String reviewId,
            String comment,
            AggregateRepository<Review> reviewRepository,
            EventBus eventBus
    ) {
        this.reviewId = new AggregateId(reviewId);
        this.comment = new Comment(comment);
        this.reviewRepository = reviewRepository;
        this.eventBus = eventBus;
    }

    @Override
    public void execute() {
        Review review = reviewRepository.find(reviewId).orElseThrow();
        review.updateComment(comment);
        reviewRepository.save(review);
        eventBus.publish(review.pullDomainEvents());
    }
}
