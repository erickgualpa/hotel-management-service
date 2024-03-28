package org.egualpam.services.hotelmanagement.application.reviews;

import org.egualpam.services.hotelmanagement.application.shared.InternalCommand;
import org.egualpam.services.hotelmanagement.domain.reviews.Comment;
import org.egualpam.services.hotelmanagement.domain.reviews.Review;
import org.egualpam.services.hotelmanagement.domain.shared.AggregateId;
import org.egualpam.services.hotelmanagement.domain.shared.AggregateRepository;
import org.egualpam.services.hotelmanagement.domain.shared.DomainEventsPublisher;

public class UpdateReview implements InternalCommand {

    private final AggregateId reviewId;
    private final Comment comment;
    private final AggregateRepository<Review> reviewRepository;
    private final DomainEventsPublisher domainEventsPublisher;

    public UpdateReview(
            String reviewId,
            String comment,
            AggregateRepository<Review> reviewRepository,
            DomainEventsPublisher domainEventsPublisher
    ) {
        this.reviewId = new AggregateId(reviewId);
        this.comment = new Comment(comment);
        this.reviewRepository = reviewRepository;
        this.domainEventsPublisher = domainEventsPublisher;
    }

    @Override
    public void execute() {
        Review review = reviewRepository.find(reviewId).orElseThrow();
        review.updateComment(comment);
        reviewRepository.save(review);
        domainEventsPublisher.publish(review.pullDomainEvents());
    }
}
