package org.egualpam.services.hotel.rating.infrastructure.cqrs;

import org.egualpam.services.hotel.rating.application.reviews.CreateReviewCommand;
import org.egualpam.services.hotel.rating.application.shared.Command;
import org.egualpam.services.hotel.rating.domain.reviews.ReviewRepository;

public class CommandFactory {

    private final ReviewRepository reviewRepository;

    public CommandFactory(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public Command createReviewCommand(
            String reviewIdentifier,
            String hotelIdentifier,
            Integer rating,
            String comment) {
        return new CreateReviewCommand(
                reviewIdentifier,
                hotelIdentifier,
                rating,
                comment,
                reviewRepository
        );
    }
}
