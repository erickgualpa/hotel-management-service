package org.egualpam.services.hotel.rating.infrastructure.cqrs;

import org.egualpam.services.hotel.rating.application.reviews.CreateReviewCommand;
import org.egualpam.services.hotel.rating.application.shared.Command;
import org.egualpam.services.hotel.rating.domain.reviews.ReviewRepository;

final class SimpleCommandBuilder implements CommandBuilder {

    private final ReviewRepository reviewRepository;

    SimpleCommandBuilder(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public Command createReview(
            String reviewIdentifier,
            String hotelIdentifier,
            Integer rating,
            String comment
    ) {
        return new CreateReviewCommand(
                reviewIdentifier,
                hotelIdentifier,
                rating,
                comment,
                reviewRepository
        );
    }
}
