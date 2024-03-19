package org.egualpam.services.hotelmanagement.infrastructure.cqrs.simple;

import lombok.RequiredArgsConstructor;
import org.egualpam.services.hotelmanagement.application.hotels.FindHotels;
import org.egualpam.services.hotelmanagement.application.hotels.HotelView;
import org.egualpam.services.hotelmanagement.application.hotels.HotelsView;
import org.egualpam.services.hotelmanagement.application.reviews.FindHotel;
import org.egualpam.services.hotelmanagement.application.reviews.FindHotelReviews;
import org.egualpam.services.hotelmanagement.application.reviews.ReviewsView;
import org.egualpam.services.hotelmanagement.application.shared.InternalQuery;
import org.egualpam.services.hotelmanagement.application.shared.Query;
import org.egualpam.services.hotelmanagement.application.shared.QueryBus;
import org.egualpam.services.hotelmanagement.application.shared.View;
import org.egualpam.services.hotelmanagement.application.shared.ViewSupplier;

import java.util.Map;

@FunctionalInterface
interface QueryHandler {
    View handle(Query query);
}

public final class SimpleQueryBus implements QueryBus {

    private final Map<Class<? extends Query>, QueryHandler> handlers;

    public SimpleQueryBus(
            ViewSupplier<HotelView> hotelViewSupplier,
            ViewSupplier<HotelsView> hotelsViewSupplier,
            ViewSupplier<ReviewsView> reviewsViewSupplier
    ) {
        handlers = Map.of(
                FindHotelReviewsQuery.class,
                new FindHotelReviewsQueryHandler(reviewsViewSupplier),
                FindHotelsQuery.class,
                new FindHotelsQueryHandler(hotelsViewSupplier),
                FindHotelQuery.class,
                new FindHotelQueryHandler(hotelViewSupplier)
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

        private final ViewSupplier<HotelsView> hotelsViewSupplier;

        @Override
        public View handle(Query query) {
            InternalQuery<HotelsView> internalQuery =
                    new FindHotels(
                            ((FindHotelsQuery) query).getLocation(),
                            ((FindHotelsQuery) query).getMinPrice(),
                            ((FindHotelsQuery) query).getMaxPrice(),
                            hotelsViewSupplier
                    );
            return internalQuery.get();
        }
    }

    @RequiredArgsConstructor
    static class FindHotelQueryHandler implements QueryHandler {

        private final ViewSupplier<HotelView> hotelViewSupplier;

        @Override
        public View handle(Query query) {
            InternalQuery<HotelView> internalQuery =
                    new FindHotel(
                            ((FindHotelQuery) query).getHotelId(),
                            hotelViewSupplier
                    );
            return internalQuery.get();
        }
    }
}