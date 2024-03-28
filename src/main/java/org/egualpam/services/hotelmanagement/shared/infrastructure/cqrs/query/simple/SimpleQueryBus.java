package org.egualpam.services.hotelmanagement.shared.infrastructure.cqrs.query.simple;

import lombok.RequiredArgsConstructor;
import org.egualpam.services.hotelmanagement.domain.hotels.HotelCriteria;
import org.egualpam.services.hotelmanagement.domain.reviews.ReviewCriteria;
import org.egualpam.services.hotelmanagement.hotels.application.HotelView;
import org.egualpam.services.hotelmanagement.hotels.application.HotelsView;
import org.egualpam.services.hotelmanagement.reviews.application.ReviewsView;
import org.egualpam.services.hotelmanagement.shared.application.Query;
import org.egualpam.services.hotelmanagement.shared.application.QueryBus;
import org.egualpam.services.hotelmanagement.shared.application.View;
import org.egualpam.services.hotelmanagement.shared.application.ViewSupplier;

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
            final FindHotelReviewsQuery findHotelReviewsQuery = (FindHotelReviewsQuery) query;
            return reviewsViewSupplier.get(
                    new ReviewCriteria(
                            findHotelReviewsQuery.getHotelIdentifier()
                    )
            );
        }
    }

    @RequiredArgsConstructor
    static class FindHotelsQueryHandler implements QueryHandler {

        private final ViewSupplier<HotelsView> hotelsViewSupplier;

        @Override
        public View handle(Query query) {
            final FindHotelsQuery findHotelsQuery = (FindHotelsQuery) query;
            return hotelsViewSupplier.get(
                    new HotelCriteria(
                            findHotelsQuery.getLocation(),
                            findHotelsQuery.getMinPrice(),
                            findHotelsQuery.getMaxPrice()
                    )
            );
        }
    }

    @RequiredArgsConstructor
    static class FindHotelQueryHandler implements QueryHandler {

        private final ViewSupplier<HotelView> hotelViewSupplier;

        @Override
        public View handle(Query query) {
            final FindHotelQuery findHotelQuery = (FindHotelQuery) query;
            return hotelViewSupplier.get(
                    new HotelCriteria(
                            findHotelQuery.getHotelId()
                    )
            );
        }
    }
}
