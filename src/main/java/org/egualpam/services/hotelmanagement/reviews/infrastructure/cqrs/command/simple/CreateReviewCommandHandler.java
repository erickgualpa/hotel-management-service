package org.egualpam.services.hotelmanagement.reviews.infrastructure.cqrs.command.simple;

import lombok.RequiredArgsConstructor;
import org.egualpam.services.hotelmanagement.reviews.application.command.CreateReview;
import org.egualpam.services.hotelmanagement.reviews.application.command.CreateReviewCommand;
import org.egualpam.services.hotelmanagement.reviews.domain.Review;
import org.egualpam.services.hotelmanagement.shared.application.command.Command;
import org.egualpam.services.hotelmanagement.shared.domain.AggregateRepository;
import org.egualpam.services.hotelmanagement.shared.domain.PublicEventBus;
import org.egualpam.services.hotelmanagement.shared.infrastructure.cqrs.command.simple.CommandHandler;

import java.util.Optional;

@RequiredArgsConstructor
public class CreateReviewCommandHandler implements CommandHandler {

    private final AggregateRepository<Review> reviewRepository;
    private final PublicEventBus publicEventBus;

    @Override
    public void handle(Command command) {
        Optional.of(command)
                .filter(CreateReviewCommand.class::isInstance)
                .map(CreateReviewCommand.class::cast)
                .map(
                        cmd -> new CreateReview(
                                cmd.getReviewIdentifier(),
                                cmd.getHotelIdentifier(),
                                cmd.getRating(),
                                cmd.getComment(),
                                reviewRepository,
                                publicEventBus
                        )
                )
                .ifPresent(CreateReview::execute);
    }
}
