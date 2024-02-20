package org.egualpam.services.hotel.rating.application.reviews;

import org.egualpam.services.hotel.rating.application.shared.InternalCommand;
import org.egualpam.services.hotel.rating.domain.reviews.Comment;
import org.egualpam.services.hotel.rating.domain.reviews.Rating;
import org.egualpam.services.hotel.rating.domain.reviews.Review;
import org.egualpam.services.hotel.rating.domain.shared.AggregateId;
import org.egualpam.services.hotel.rating.domain.shared.AggregateRepository;
import org.egualpam.services.hotel.rating.domain.shared.DomainEventsPublisher;
import org.egualpam.services.hotel.rating.domain.shared.Identifier;

public final class CreateReview implements InternalCommand {

    private final AggregateId reviewId;
    private final Identifier hotelIdentifier;
    private final Rating rating;
    private final Comment comment;

    private final AggregateRepository<Review> aggregateRepository;
    private final DomainEventsPublisher domainEventsPublisher;

    public CreateReview(
            String reviewId,
            String hotelIdentifier,
            Integer rating,
            String comment,
            AggregateRepository<Review> aggregateRepository,
            DomainEventsPublisher domainEventsPublisher
    ) {
        this.reviewId = new AggregateId(reviewId);
        this.hotelIdentifier = new Identifier(hotelIdentifier);
        this.rating = new Rating(rating);
        this.comment = new Comment(comment);
        this.aggregateRepository = aggregateRepository;
        this.domainEventsPublisher = domainEventsPublisher;
    }

    @Override
    public void execute() {
        Review review = new Review(
                reviewId,
                hotelIdentifier,
                rating,
                comment
        );
        aggregateRepository.save(review);
        domainEventsPublisher.publish(review.getDomainEvents());
    }
}
