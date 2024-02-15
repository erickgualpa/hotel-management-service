package org.egualpam.services.hotel.rating.infrastructure.cqrs;

import org.egualpam.services.hotel.rating.domain.reviews.ReviewRepository;
import org.egualpam.services.hotel.rating.infrastructure.controller.CreateReviewCommand;

public final class SimpleCommandBus implements CommandBus {

    private final ReviewRepository reviewRepository;

    public SimpleCommandBus(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public void publish(Command command) {
        if (command instanceof CreateReviewCommand) {
            org.egualpam.services.hotel.rating.application.reviews.CreateReviewCommand createReviewCommand =
                    new org.egualpam.services.hotel.rating.application.reviews.CreateReviewCommand(
                            ((CreateReviewCommand) command).getReviewIdentifier(),
                            ((CreateReviewCommand) command).getHotelIdentifier(),
                            ((CreateReviewCommand) command).getRating(),
                            ((CreateReviewCommand) command).getComment(),
                            reviewRepository
                    );
            createReviewCommand.execute();
        }
    }
}
