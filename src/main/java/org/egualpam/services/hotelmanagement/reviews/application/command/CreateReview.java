package org.egualpam.services.hotelmanagement.reviews.application.command;

import org.egualpam.services.hotelmanagement.reviews.domain.Review;
import org.egualpam.services.hotelmanagement.shared.application.command.InternalCommand;
import org.egualpam.services.hotelmanagement.shared.domain.AggregateRepository;
import org.egualpam.services.hotelmanagement.shared.domain.EventBus;

public final class CreateReview implements InternalCommand {

    private final String reviewId;
    private final String hotelId;
    private final Integer rating;
    private final String comment;

    private final AggregateRepository<Review> reviewRepository;
    private final EventBus eventBus;

    public CreateReview(
            String reviewId,
            String hotelId,
            Integer rating,
            String comment,
            AggregateRepository<Review> reviewRepository,
            EventBus eventBus
    ) {
        this.reviewId = reviewId;
        this.hotelId = hotelId;
        this.rating = rating;
        this.comment = comment;
        this.reviewRepository = reviewRepository;
        this.eventBus = eventBus;
    }

    @Override
    public void execute() {
        Review review = Review.create(
                reviewRepository,
                reviewId,
                hotelId,
                rating,
                comment
        );
        reviewRepository.save(review);
        eventBus.publish(review.pullDomainEvents());
    }
}
