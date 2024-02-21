package org.egualpam.services.hotel.rating.infrastructure.cqrs.simple;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.egualpam.services.hotel.rating.application.hotels.FindHotels;
import org.egualpam.services.hotel.rating.application.hotels.HotelsView;
import org.egualpam.services.hotel.rating.application.reviews.FindHotelReviews;
import org.egualpam.services.hotel.rating.application.reviews.ReviewsView;
import org.egualpam.services.hotel.rating.application.shared.InternalQuery;
import org.egualpam.services.hotel.rating.application.shared.Query;
import org.egualpam.services.hotel.rating.application.shared.QueryBus;
import org.egualpam.services.hotel.rating.domain.hotels.Hotel;
import org.egualpam.services.hotel.rating.domain.reviews.Review;
import org.egualpam.services.hotel.rating.domain.shared.AggregateRepository;

import java.util.Map;

@FunctionalInterface
interface QueryHandler {
    String handle(Query query);
}

public final class SimpleQueryBus implements QueryBus {

    private final Map<Class<? extends Query>, QueryHandler> handlers;

    public SimpleQueryBus(
            ObjectMapper objectMapper,
            AggregateRepository<Hotel> aggregateHotelRepository,
            AggregateRepository<Review> aggregateReviewRepository
    ) {
        handlers = Map.of(
                FindHotelReviewsQuery.class,
                new FindHotelReviewsQueryHandler(objectMapper, aggregateReviewRepository),
                FindHotelsQuery.class,
                new FindHotelsQueryHandler(objectMapper, aggregateHotelRepository)
        );
    }

    @Override
    public String publish(Query query) {
        QueryHandler queryHandler = handlers.get(query.getClass());
        if (queryHandler == null) {
            throw new QueryHandlerNotFound();
        }
        return queryHandler.handle(query);
    }

    static class FindHotelReviewsQueryHandler implements QueryHandler {

        private final ObjectMapper objectMapper;
        private final AggregateRepository<Review> aggregateReviewRepository;

        public FindHotelReviewsQueryHandler(
                ObjectMapper objectMapper,
                AggregateRepository<Review> aggregateReviewRepository
        ) {
            this.objectMapper = objectMapper;
            this.aggregateReviewRepository = aggregateReviewRepository;
        }

        @Override
        public String handle(Query query) {
            InternalQuery<ReviewsView> internalQuery =
                    new FindHotelReviews(
                            ((FindHotelReviewsQuery) query).getHotelIdentifier(),
                            aggregateReviewRepository
                    );

            ReviewsView reviewsView = internalQuery.get();

            try {
                return objectMapper.writeValueAsString(reviewsView);
            } catch (JsonProcessingException e) {
                throw new OutcomeSerializationFailed(e);
            }
        }
    }

    static class FindHotelsQueryHandler implements QueryHandler {

        private final ObjectMapper objectMapper;
        private final AggregateRepository<Hotel> aggregateHotelRepository;

        public FindHotelsQueryHandler(
                ObjectMapper objectMapper,
                AggregateRepository<Hotel> aggregateHotelRepository
        ) {
            this.objectMapper = objectMapper;
            this.aggregateHotelRepository = aggregateHotelRepository;
        }

        @Override
        public String handle(Query query) {
            InternalQuery<HotelsView> internalQuery =
                    new FindHotels(
                            ((FindHotelsQuery) query).getLocation(),
                            ((FindHotelsQuery) query).getMinPrice(),
                            ((FindHotelsQuery) query).getMaxPrice(),
                            aggregateHotelRepository
                    );

            HotelsView hotelsView = internalQuery.get();

            try {
                return objectMapper.writeValueAsString(hotelsView);
            } catch (JsonProcessingException e) {
                throw new OutcomeSerializationFailed(e);
            }
        }
    }
}
