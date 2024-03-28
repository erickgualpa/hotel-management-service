package org.egualpam.services.hotelmanagement.reviews.application;

import org.egualpam.services.hotelmanagement.domain.reviews.Review;
import org.egualpam.services.hotelmanagement.shared.application.InternalCommand;
import org.egualpam.services.hotelmanagement.shared.domain.AggregateRepository;
import org.egualpam.services.hotelmanagement.shared.domain.PublicEventBus;

public final class CreateReview implements InternalCommand {

    private final String reviewId;
    private final String hotelId;
    private final Integer rating;
    private final String comment;

    private final AggregateRepository<Review> reviewRepository;
    private final PublicEventBus publicEventBus;

    public CreateReview(
            String reviewId,
            String hotelId,
            Integer rating,
            String comment,
            AggregateRepository<Review> reviewRepository,
            PublicEventBus publicEventBus
    ) {
        this.reviewId = reviewId;
        this.hotelId = hotelId;
        this.rating = rating;
        this.comment = comment;
        this.reviewRepository = reviewRepository;
        this.publicEventBus = publicEventBus;
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
        publicEventBus.publish(review.pullDomainEvents());
    }
}
