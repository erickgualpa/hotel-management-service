package org.egualpam.services.hotelmanagement.shared.infrastructure.cqrs.query.simple;

import lombok.RequiredArgsConstructor;
import org.egualpam.services.hotelmanagement.hotels.application.query.MultipleHotelsView;
import org.egualpam.services.hotelmanagement.hotels.application.query.SingleHotelView;
import org.egualpam.services.hotelmanagement.hotels.domain.HotelCriteria;
import org.egualpam.services.hotelmanagement.reviews.application.query.MultipleReviewsView;
import org.egualpam.services.hotelmanagement.reviews.domain.ReviewCriteria;
import org.egualpam.services.hotelmanagement.shared.application.query.Query;
import org.egualpam.services.hotelmanagement.shared.application.query.QueryBus;
import org.egualpam.services.hotelmanagement.shared.application.query.View;
import org.egualpam.services.hotelmanagement.shared.application.query.ViewSupplier;

import java.util.Map;

@FunctionalInterface
interface QueryHandler {
    View handle(Query query);
}

public final class SimpleQueryBus implements QueryBus {

    private final Map<Class<? extends Query>, QueryHandler> handlers;

    public SimpleQueryBus(
            ViewSupplier<SingleHotelView> singleHotelViewSupplier,
            ViewSupplier<MultipleHotelsView> multipleHotelsViewSupplier,
            ViewSupplier<MultipleReviewsView> multipleReviewsViewSupplier
    ) {
        handlers = Map.of(
                FindHotelReviewsQuery.class,
                new FindHotelReviewsQueryHandler(multipleReviewsViewSupplier),
                FindHotelsQuery.class,
                new FindHotelsQueryHandler(multipleHotelsViewSupplier),
                FindHotelQuery.class,
                new FindHotelQueryHandler(singleHotelViewSupplier)
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

        private final ViewSupplier<MultipleReviewsView> multipleReviewsViewSupplier;

        @Override
        public View handle(Query query) {
            final FindHotelReviewsQuery findHotelReviewsQuery = (FindHotelReviewsQuery) query;
            return multipleReviewsViewSupplier.get(
                    new ReviewCriteria(
                            findHotelReviewsQuery.getHotelIdentifier()
                    )
            );
        }
    }

    @RequiredArgsConstructor
    static class FindHotelsQueryHandler implements QueryHandler {

        private final ViewSupplier<MultipleHotelsView> multipleHotelsViewSupplier;

        @Override
        public View handle(Query query) {
            final FindHotelsQuery findHotelsQuery = (FindHotelsQuery) query;
            return multipleHotelsViewSupplier.get(
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

        private final ViewSupplier<SingleHotelView> singleHotelViewSupplier;

        @Override
        public View handle(Query query) {
            final FindHotelQuery findHotelQuery = (FindHotelQuery) query;
            return singleHotelViewSupplier.get(
                    new HotelCriteria(
                            findHotelQuery.getHotelId()
                    )
            );
        }
    }
}
