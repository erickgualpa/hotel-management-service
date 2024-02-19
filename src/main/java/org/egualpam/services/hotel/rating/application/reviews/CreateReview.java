package org.egualpam.services.hotel.rating.application.reviews;

import org.egualpam.services.hotel.rating.application.shared.InternalCommand;
import org.egualpam.services.hotel.rating.domain.reviews.Review;
import org.egualpam.services.hotel.rating.domain.reviews.ReviewRepository;
import org.egualpam.services.hotel.rating.domain.shared.Comment;
import org.egualpam.services.hotel.rating.domain.shared.DomainEventsPublisher;
import org.egualpam.services.hotel.rating.domain.shared.Identifier;
import org.egualpam.services.hotel.rating.domain.shared.Rating;

public final class CreateReview implements InternalCommand {

    private final Identifier reviewIdentifier;
    private final Identifier hotelIdentifier;
    private final Rating rating;
    private final Comment comment;

    private final ReviewRepository reviewRepository;
    private final DomainEventsPublisher domainEventsPublisher;

    public CreateReview(
            String reviewIdentifier,
            String hotelIdentifier,
            Integer rating,
            String comment,
            ReviewRepository reviewRepository,
            DomainEventsPublisher domainEventsPublisher
    ) {
        this.reviewIdentifier = new Identifier(reviewIdentifier);
        this.hotelIdentifier = new Identifier(hotelIdentifier);
        this.rating = new Rating(rating);
        this.comment = new Comment(comment);
        this.reviewRepository = reviewRepository;
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
        reviewRepository.save(review);
        domainEventsPublisher.publish(review.getDomainEvents());
    }
}
