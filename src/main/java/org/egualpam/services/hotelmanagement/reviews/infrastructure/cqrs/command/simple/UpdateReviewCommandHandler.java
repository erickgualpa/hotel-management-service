package org.egualpam.services.hotelmanagement.reviews.infrastructure.cqrs.command.simple;

import lombok.RequiredArgsConstructor;
import org.egualpam.services.hotelmanagement.reviews.application.command.UpdateReview;
import org.egualpam.services.hotelmanagement.reviews.application.command.UpdateReviewCommand;
import org.egualpam.services.hotelmanagement.reviews.domain.Review;
import org.egualpam.services.hotelmanagement.shared.application.command.Command;
import org.egualpam.services.hotelmanagement.shared.domain.AggregateRepository;
import org.egualpam.services.hotelmanagement.shared.domain.PublicEventBus;
import org.egualpam.services.hotelmanagement.shared.infrastructure.cqrs.command.simple.CommandHandler;

@RequiredArgsConstructor
public class UpdateReviewCommandHandler implements CommandHandler {

    private final AggregateRepository<Review> reviewRepository;
    private final PublicEventBus publicEventBus;

    @Override
    public void handle(Command command) {
        final UpdateReviewCommand updateReviewCommand = (UpdateReviewCommand) command;
        new UpdateReview(
                updateReviewCommand.getReviewIdentifier(),
                updateReviewCommand.getComment(),
                reviewRepository,
                publicEventBus
        ).execute();
    }
}
