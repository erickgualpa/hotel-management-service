package org.egualpam.services.hotel.rating.infrastructure.cqrs.simple;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.egualpam.services.hotel.rating.application.hotels.FindHotels;
import org.egualpam.services.hotel.rating.application.hotels.HotelDto;
import org.egualpam.services.hotel.rating.application.reviews.FindHotelReviews;
import org.egualpam.services.hotel.rating.application.reviews.ReviewDto;
import org.egualpam.services.hotel.rating.application.shared.InternalQuery;
import org.egualpam.services.hotel.rating.application.shared.Query;
import org.egualpam.services.hotel.rating.application.shared.QueryBus;
import org.egualpam.services.hotel.rating.domain.hotels.HotelRepository;
import org.egualpam.services.hotel.rating.domain.reviews.Review;
import org.egualpam.services.hotel.rating.domain.shared.AggregateRepository;

import java.util.List;
import java.util.Map;

@FunctionalInterface
interface QueryHandler {
    String handle(Query query);
}

public final class SimpleQueryBus implements QueryBus {

    private final Map<Class<? extends Query>, QueryHandler> handlers;

    public SimpleQueryBus(
            ObjectMapper objectMapper,
            HotelRepository hotelRepository,
            AggregateRepository<Review> aggregateReviewRepository
    ) {
        handlers = Map.of(
                FindHotelReviewsQuery.class,
                new FindHotelReviewsQueryHandler(objectMapper, aggregateReviewRepository),
                FindHotelsQuery.class,
                new FindHotelsQueryHandler(objectMapper, hotelRepository)
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
            InternalQuery<List<ReviewDto>> internalQuery =
                    new FindHotelReviews(
                            ((FindHotelReviewsQuery) query).getHotelIdentifier(),
                            aggregateReviewRepository
                    );

            List<ReviewDto> reviewsDto = internalQuery.get();

            try {
                return objectMapper.writeValueAsString(reviewsDto);
            } catch (JsonProcessingException e) {
                throw new OutcomeSerializationFailed(e);
            }
        }
    }

    static class FindHotelsQueryHandler implements QueryHandler {

        private final ObjectMapper objectMapper;
        private final HotelRepository hotelRepository;

        public FindHotelsQueryHandler(ObjectMapper objectMapper, HotelRepository hotelRepository) {
            this.objectMapper = objectMapper;
            this.hotelRepository = hotelRepository;
        }

        @Override
        public String handle(Query query) {
            InternalQuery<List<HotelDto>> internalQuery =
                    new FindHotels(
                            ((FindHotelsQuery) query).getLocation(),
                            ((FindHotelsQuery) query).getMinPrice(),
                            ((FindHotelsQuery) query).getMaxPrice(),
                            hotelRepository
                    );

            List<HotelDto> hotelsDto = internalQuery.get();

            try {
                return objectMapper.writeValueAsString(hotelsDto);
            } catch (JsonProcessingException e) {
                throw new OutcomeSerializationFailed(e);
            }
        }
    }
}
