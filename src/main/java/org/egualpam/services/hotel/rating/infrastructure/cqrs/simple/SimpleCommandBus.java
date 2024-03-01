package org.egualpam.services.hotel.rating.infrastructure.cqrs.simple;

import lombok.RequiredArgsConstructor;
import org.egualpam.services.hotel.rating.application.reviews.CreateReview;
import org.egualpam.services.hotel.rating.application.reviews.UpdateReview;
import org.egualpam.services.hotel.rating.application.shared.Command;
import org.egualpam.services.hotel.rating.application.shared.CommandBus;
import org.egualpam.services.hotel.rating.application.shared.InternalCommand;
import org.egualpam.services.hotel.rating.domain.reviews.Review;
import org.egualpam.services.hotel.rating.domain.shared.AggregateRepository;
import org.egualpam.services.hotel.rating.domain.shared.DomainEventsPublisher;

import java.util.Map;

@FunctionalInterface
interface CommandHandler {
    void handle(Command command);
}

public final class SimpleCommandBus implements CommandBus {

    private final Map<Class<? extends Command>, CommandHandler> handlers;

    public SimpleCommandBus(
            AggregateRepository<Review> aggregateReviewRepository,
            DomainEventsPublisher domainEventsPublisher
    ) {
        handlers = Map.of(
                CreateReviewCommand.class,
                new CreateReviewCommandHandler(
                        aggregateReviewRepository,
                        domainEventsPublisher
                ),
                UpdateReviewCommand.class,
                new UpdateReviewCommandHandler(
                        aggregateReviewRepository,
                        domainEventsPublisher
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

        private final AggregateRepository<Review> aggregateReviewRepository;
        private final DomainEventsPublisher domainEventsPublisher;

        @Override
        public void handle(Command command) {
            InternalCommand internalCommand =
                    new CreateReview(
                            ((CreateReviewCommand) command).getReviewIdentifier(),
                            ((CreateReviewCommand) command).getHotelIdentifier(),
                            ((CreateReviewCommand) command).getRating(),
                            ((CreateReviewCommand) command).getComment(),
                            aggregateReviewRepository,
                            domainEventsPublisher
                    );
            internalCommand.execute();
        }
    }

    @RequiredArgsConstructor
    static class UpdateReviewCommandHandler implements CommandHandler {

        private final AggregateRepository<Review> aggregateReviewRepository;
        private final DomainEventsPublisher domainEventsPublisher;

        @Override
        public void handle(Command command) {
            InternalCommand internalCommand =
                    new UpdateReview(
                            ((UpdateReviewCommand) command).getReviewIdentifier(),
                            ((UpdateReviewCommand) command).getComment(),
                            aggregateReviewRepository,
                            domainEventsPublisher);
            internalCommand.execute();
        }
    }
}
