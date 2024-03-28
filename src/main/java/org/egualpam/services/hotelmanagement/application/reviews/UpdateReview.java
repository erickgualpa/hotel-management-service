package org.egualpam.services.hotelmanagement.application.reviews;

import org.egualpam.services.hotelmanagement.domain.reviews.Comment;
import org.egualpam.services.hotelmanagement.domain.reviews.Review;
import org.egualpam.services.hotelmanagement.domain.shared.AggregateId;
import org.egualpam.services.hotelmanagement.domain.shared.AggregateRepository;
import org.egualpam.services.hotelmanagement.domain.shared.PublicEventBus;
import org.egualpam.services.hotelmanagement.shared.application.InternalCommand;

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
