package org.egualpam.services.hotel.rating.infrastructure.cqrs;

import org.egualpam.services.hotel.rating.application.shared.Command;
import org.egualpam.services.hotel.rating.domain.reviews.ReviewRepository;

public class SimpleCommandBus implements CommandBus {

    private final ReviewRepository reviewRepository;

    public SimpleCommandBus(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public CommandBuilder commandBuilder() {
        return new SimpleCommandBuilder(reviewRepository);
    }

    @Override
    public void publish(Command command) {
        command.execute();
    }
}
