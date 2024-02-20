package org.egualpam.services.hotel.rating.application.reviews;

import org.egualpam.services.hotel.rating.application.shared.InternalCommand;
import org.egualpam.services.hotel.rating.domain.reviews.Review;
import org.egualpam.services.hotel.rating.domain.shared.AggregateRepository;
import org.egualpam.services.hotel.rating.domain.shared.Comment;
import org.egualpam.services.hotel.rating.domain.shared.DomainEventsPublisher;
import org.egualpam.services.hotel.rating.domain.shared.Identifier;
import org.egualpam.services.hotel.rating.domain.shared.Rating;

public final class CreateReview implements InternalCommand {

    private final Identifier reviewIdentifier;
    private final Identifier hotelIdentifier;
    private final Rating rating;
    private final Comment comment;

    private final AggregateRepository<Review> aggregateRepository;
    private final DomainEventsPublisher domainEventsPublisher;

    public CreateReview(
            String reviewIdentifier,
            String hotelIdentifier,
            Integer rating,
            String comment,
            AggregateRepository<Review> aggregateRepository,
            DomainEventsPublisher domainEventsPublisher
    ) {
        this.reviewIdentifier = new Identifier(reviewIdentifier);
        this.hotelIdentifier = new Identifier(hotelIdentifier);
        this.rating = new Rating(rating);
        this.comment = new Comment(comment);
        this.aggregateRepository = aggregateRepository;
        this.domainEventsPublisher = domainEventsPublisher;
    }

    @Override
    public void execute() {
        Review review = new Review(
                reviewIdentifier,
                hotelIdentifier,
                rating,
                comment
        );
        aggregateRepository.save(review);
        domainEventsPublisher.publish(review.getDomainEvents());
    }
}
