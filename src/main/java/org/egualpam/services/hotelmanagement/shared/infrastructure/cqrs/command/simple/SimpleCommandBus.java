package org.egualpam.services.hotelmanagement.shared.infrastructure.cqrs.command.simple;

import lombok.RequiredArgsConstructor;
import org.egualpam.services.hotelmanagement.reviews.application.command.CreateReview;
import org.egualpam.services.hotelmanagement.reviews.application.command.UpdateReview;
import org.egualpam.services.hotelmanagement.reviews.domain.Review;
import org.egualpam.services.hotelmanagement.shared.application.command.Command;
import org.egualpam.services.hotelmanagement.shared.application.command.CommandBus;
import org.egualpam.services.hotelmanagement.shared.application.command.InternalCommand;
import org.egualpam.services.hotelmanagement.shared.domain.AggregateRepository;
import org.egualpam.services.hotelmanagement.shared.domain.PublicEventBus;

import java.util.Map;

@FunctionalInterface
interface CommandHandler {
    void handle(Command command);
}

public final class SimpleCommandBus implements CommandBus {

    private final Map<Class<? extends Command>, CommandHandler> handlers;

    public SimpleCommandBus(
            AggregateRepository<Review> reviewRepository,
            PublicEventBus publicEventBus
    ) {
        handlers = Map.of(
                CreateReviewCommand.class,
                new CreateReviewCommandHandler(
                        reviewRepository,
                        publicEventBus
                ),
                UpdateReviewCommand.class,
                new UpdateReviewCommandHandler(
                        reviewRepository,
                        publicEventBus
                )
        );
    }

    @Override
    public void publish(Command command) {
        CommandHandler commandHandler = handlers.get(command.getClass());
        if (commandHandler == null) {
            throw new CommandHandlerNotFound();
        }
        commandHandler.handle(command);
    }

    @RequiredArgsConstructor
    static class CreateReviewCommandHandler implements CommandHandler {

        private final AggregateRepository<Review> reviewRepository;
        private final PublicEventBus publicEventBus;

        @Override
        public void handle(Command command) {
            final CreateReviewCommand createReviewCommand = (CreateReviewCommand) command;
            InternalCommand internalCommand =
                    new CreateReview(
                            createReviewCommand.getReviewIdentifier(),
                            createReviewCommand.getHotelIdentifier(),
                            createReviewCommand.getRating(),
                            createReviewCommand.getComment(),
                            reviewRepository,
                            publicEventBus
                    );
            internalCommand.execute();
        }
    }

    @RequiredArgsConstructor
    static class UpdateReviewCommandHandler implements CommandHandler {

        private final AggregateRepository<Review> reviewRepository;
        private final PublicEventBus publicEventBus;

        @Override
        public void handle(Command command) {
            final UpdateReviewCommand updateReviewCommand = (UpdateReviewCommand) command;
            InternalCommand internalCommand =
                    new UpdateReview(
                            updateReviewCommand.getReviewIdentifier(),
                            updateReviewCommand.getComment(),
                            reviewRepository,
                            publicEventBus);
            internalCommand.execute();
        }
    }
}
