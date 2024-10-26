package org.egualpam.contexts.hotelmanagement.review.infrastructure.cqrs.command.simple;

import lombok.RequiredArgsConstructor;
import org.egualpam.contexts.hotelmanagement.review.application.command.UpdateReview;
import org.egualpam.contexts.hotelmanagement.review.application.command.UpdateReviewCommand;
import org.egualpam.contexts.hotelmanagement.review.domain.Review;
import org.egualpam.contexts.hotelmanagement.shared.application.command.Command;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateRepository;
import org.egualpam.contexts.hotelmanagement.shared.domain.EventBus;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.command.simple.CommandHandler;

import java.util.Optional;

@RequiredArgsConstructor
public class UpdateReviewCommandHandler implements CommandHandler {

    private final AggregateRepository<Review> reviewRepository;
    private final EventBus eventBus;

    @Override
    public void handle(Command command) {
        Optional.of(command)
                .filter(UpdateReviewCommand.class::isInstance)
                .map(UpdateReviewCommand.class::cast)
                .map(
                        cmd -> new UpdateReview(
                                cmd.getReviewIdentifier(),
                                cmd.getComment(),
                                reviewRepository,
                                eventBus
                        )
                )
                .ifPresent(UpdateReview::execute);
    }
}
