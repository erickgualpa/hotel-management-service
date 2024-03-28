package org.egualpam.services.hotelmanagement.infrastructure.cqrs.simple;

import lombok.RequiredArgsConstructor;
import org.egualpam.services.hotelmanagement.application.reviews.CreateReview;
import org.egualpam.services.hotelmanagement.application.reviews.UpdateReview;
import org.egualpam.services.hotelmanagement.application.shared.Command;
import org.egualpam.services.hotelmanagement.application.shared.CommandBus;
import org.egualpam.services.hotelmanagement.application.shared.InternalCommand;
import org.egualpam.services.hotelmanagement.domain.reviews.Review;
import org.egualpam.services.hotelmanagement.domain.shared.AggregateRepository;
import org.egualpam.services.hotelmanagement.domain.shared.DomainEventsBus;

import java.util.Map;

@FunctionalInterface
interface CommandHandler {
    void handle(Command command);
}

public final class SimpleCommandBus implements CommandBus {

    private final Map<Class<? extends Command>, CommandHandler> handlers;

    public SimpleCommandBus(
            AggregateRepository<Review> reviewRepository,
            DomainEventsBus domainEventsBus
    ) {
        handlers = Map.of(
                CreateReviewCommand.class,
                new CreateReviewCommandHandler(
                        reviewRepository,
                        domainEventsBus
                ),
                UpdateReviewCommand.class,
                new UpdateReviewCommandHandler(
                        reviewRepository,
                        domainEventsBus
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
        private final DomainEventsBus domainEventsBus;

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
                            domainEventsBus
                    );
            internalCommand.execute();
        }
    }

    @RequiredArgsConstructor
    static class UpdateReviewCommandHandler implements CommandHandler {

        private final AggregateRepository<Review> reviewRepository;
        private final DomainEventsBus domainEventsBus;

        @Override
        public void handle(Command command) {
            final UpdateReviewCommand updateReviewCommand = (UpdateReviewCommand) command;
            InternalCommand internalCommand =
                    new UpdateReview(
                            updateReviewCommand.getReviewIdentifier(),
                            updateReviewCommand.getComment(),
                            reviewRepository,
                            domainEventsBus);
            internalCommand.execute();
        }
    }
}
