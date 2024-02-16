package org.egualpam.services.hotel.rating.infrastructure.cqrs;

import org.egualpam.services.hotel.rating.application.reviews.CreateReview;
import org.egualpam.services.hotel.rating.application.shared.InternalCommand;
import org.egualpam.services.hotel.rating.domain.reviews.ReviewRepository;
import org.egualpam.services.hotel.rating.infrastructure.controller.CreateReviewCommand;

public final class SimpleCommandBus implements CommandBus {

    private final ReviewRepository reviewRepository;

    public SimpleCommandBus(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public void publish(Command command) {
        if (command instanceof CreateReviewCommand createReviewCommand) {
            InternalCommand internalCommand =
                    new CreateReview(
                            createReviewCommand.getReviewIdentifier(),
                            createReviewCommand.getHotelIdentifier(),
                            createReviewCommand.getRating(),
                            createReviewCommand.getComment(),
                            reviewRepository
                    );
            internalCommand.execute();
        }
    }
}
