package org.egualpam.services.hotelmanagement.reviews.application.command;

import org.egualpam.services.hotelmanagement.reviews.domain.Comment;
import org.egualpam.services.hotelmanagement.reviews.domain.Review;
import org.egualpam.services.hotelmanagement.shared.application.command.InternalCommand;
import org.egualpam.services.hotelmanagement.shared.domain.AggregateId;
import org.egualpam.services.hotelmanagement.shared.domain.AggregateRepository;
import org.egualpam.services.hotelmanagement.shared.domain.PublicEventBus;

public class UpdateReview implements InternalCommand {

    private final AggregateId reviewId;
    private final Comment comment;
    private final AggregateRepository<Review> reviewRepository;
    private final PublicEventBus publicEventBus;

    public UpdateReview(
            String reviewId,
            String comment,
            AggregateRepository<Review> reviewRepository,
            PublicEventBus publicEventBus
    ) {
        this.reviewId = new AggregateId(reviewId);
        this.comment = new Comment(comment);
        this.reviewRepository = reviewRepository;
        this.publicEventBus = publicEventBus;
    }

    @Override
    public void execute() {
        Review review = reviewRepository.find(reviewId).orElseThrow();
        review.updateComment(comment);
        reviewRepository.save(review);
        publicEventBus.publish(review.pullDomainEvents());
    }
}
