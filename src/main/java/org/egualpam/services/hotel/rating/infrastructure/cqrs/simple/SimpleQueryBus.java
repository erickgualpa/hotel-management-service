package org.egualpam.services.hotel.rating.infrastructure.cqrs.simple;

import lombok.RequiredArgsConstructor;
import org.egualpam.services.hotel.rating.application.hotels.FindHotels;
import org.egualpam.services.hotel.rating.application.hotels.HotelView;
import org.egualpam.services.hotel.rating.application.hotels.HotelsView;
import org.egualpam.services.hotel.rating.application.reviews.FindHotel;
import org.egualpam.services.hotel.rating.application.reviews.FindHotelReviews;
import org.egualpam.services.hotel.rating.application.reviews.ReviewsView;
import org.egualpam.services.hotel.rating.application.shared.InternalQuery;
import org.egualpam.services.hotel.rating.application.shared.Query;
import org.egualpam.services.hotel.rating.application.shared.QueryBus;
import org.egualpam.services.hotel.rating.application.shared.View;
import org.egualpam.services.hotel.rating.application.shared.ViewSupplier;
import org.egualpam.services.hotel.rating.domain.hotels.Hotel;
import org.egualpam.services.hotel.rating.domain.shared.AggregateRepository;

import java.util.Map;

@FunctionalInterface
interface QueryHandler {
    View handle(Query query);
}

public final class SimpleQueryBus implements QueryBus {

    private final Map<Class<? extends Query>, QueryHandler> handlers;

    public SimpleQueryBus(
            AggregateRepository<Hotel> aggregateHotelRepository,
            ViewSupplier<ReviewsView> reviewsViewSupplier
    ) {
        handlers = Map.of(
                FindHotelReviewsQuery.class,
                new FindHotelReviewsQueryHandler(reviewsViewSupplier),
                FindHotelsQuery.class,
                new FindHotelsQueryHandler(aggregateHotelRepository),
                FindHotelQuery.class,
                new FindHotelQueryHandler(aggregateHotelRepository)
        );
    }

    @Override
    public View publish(Query query) {
        QueryHandler queryHandler = handlers.get(query.getClass());
        if (queryHandler == null) {
            throw new QueryHandlerNotFound();
        }
        return queryHandler.handle(query);
    }

    @RequiredArgsConstructor
    static class FindHotelReviewsQueryHandler implements QueryHandler {

        private final ViewSupplier<ReviewsView> reviewsViewSupplier;

        @Override
        public View handle(Query query) {
            InternalQuery<ReviewsView> internalQuery =
                    new FindHotelReviews(
                            ((FindHotelReviewsQuery) query).getHotelIdentifier(),
                            reviewsViewSupplier
                    );
            return internalQuery.get();
        }
    }

    @RequiredArgsConstructor
    static class FindHotelsQueryHandler implements QueryHandler {

        private final AggregateRepository<Hotel> aggregateHotelRepository;

        @Override
        public View handle(Query query) {
            InternalQuery<HotelsView> internalQuery =
                    new FindHotels(
                            ((FindHotelsQuery) query).getLocation(),
                            ((FindHotelsQuery) query).getMinPrice(),
                            ((FindHotelsQuery) query).getMaxPrice(),
                            aggregateHotelRepository
                    );
            return internalQuery.get();
        }
    }

    @RequiredArgsConstructor
    static class FindHotelQueryHandler implements QueryHandler {

        private final AggregateRepository<Hotel> aggregateHotelRepository;

        @Override
        public View handle(Query query) {
            InternalQuery<HotelView> internalQuery =
                    new FindHotel(
                            ((FindHotelQuery) query).getHotelId(),
                            aggregateHotelRepository
                    );
            return internalQuery.get();
        }
    }
}
