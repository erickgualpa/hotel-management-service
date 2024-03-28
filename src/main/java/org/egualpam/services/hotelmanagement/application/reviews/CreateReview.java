package org.egualpam.services.hotelmanagement.application.reviews;

import org.egualpam.services.hotelmanagement.application.shared.InternalCommand;
import org.egualpam.services.hotelmanagement.domain.reviews.Comment;
import org.egualpam.services.hotelmanagement.domain.reviews.HotelId;
import org.egualpam.services.hotelmanagement.domain.reviews.Rating;
import org.egualpam.services.hotelmanagement.domain.reviews.Review;
import org.egualpam.services.hotelmanagement.domain.reviews.exception.ReviewAlreadyExists;
import org.egualpam.services.hotelmanagement.domain.shared.AggregateId;
import org.egualpam.services.hotelmanagement.domain.shared.AggregateRepository;
import org.egualpam.services.hotelmanagement.domain.shared.DomainEventsPublisher;

public final class CreateReview implements InternalCommand {

    private final AggregateId reviewId;
    private final HotelId hotelIdentifier;
    private final Rating rating;
    private final Comment comment;

    private final AggregateRepository<Review> reviewRepository;
    private final DomainEventsPublisher domainEventsPublisher;

    public CreateReview(
            String reviewId,
            String hotelIdentifier,
            Integer rating,
            String comment,
            AggregateRepository<Review> reviewRepository,
            DomainEventsPublisher domainEventsPublisher
    ) {
        this.reviewId = new AggregateId(reviewId);
        this.hotelIdentifier = new HotelId(hotelIdentifier);
        this.rating = new Rating(rating);
        this.comment = new Comment(comment);
        this.reviewRepository = reviewRepository;
        this.domainEventsPublisher = domainEventsPublisher;
    }

    @Override
    public void execute() {
        reviewRepository.find(reviewId)
                .ifPresent(
                        review -> {
                            throw new ReviewAlreadyExists();
                        }
                );
        Review review = Review.create(
                reviewId,
                hotelIdentifier,
                rating,
                comment
        );
        reviewRepository.save(review);
        domainEventsPublisher.publish(review.pullDomainEvents());
    }
}
