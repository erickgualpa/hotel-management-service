package org.egualpam.services.hotel.rating.application.reviews;

import org.egualpam.services.hotel.rating.application.shared.InternalCommand;
import org.egualpam.services.hotel.rating.domain.reviews.Comment;
import org.egualpam.services.hotel.rating.domain.reviews.Review;
import org.egualpam.services.hotel.rating.domain.shared.AggregateId;
import org.egualpam.services.hotel.rating.domain.shared.AggregateRepository;
import org.egualpam.services.hotel.rating.domain.shared.DomainEventsPublisher;

public class UpdateReview implements InternalCommand {

    private final AggregateId reviewId;
    private final Comment comment;
    private final AggregateRepository<Review> aggregateReviewRepository;
    private final DomainEventsPublisher domainEventsPublisher;

    public UpdateReview(
            String reviewId,
            String comment,
            AggregateRepository<Review> aggregateReviewRepository,
            DomainEventsPublisher domainEventsPublisher
    ) {
        this.reviewId = new AggregateId(reviewId);
        this.comment = new Comment(comment);
        this.aggregateReviewRepository = aggregateReviewRepository;
        this.domainEventsPublisher = domainEventsPublisher;
    }

    @Override
    public void execute() {
        Review review = aggregateReviewRepository.find(reviewId);
        review.updateComment(comment);
        aggregateReviewRepository.save(review);
        domainEventsPublisher.publish(review.getDomainEvents());
    }
}
