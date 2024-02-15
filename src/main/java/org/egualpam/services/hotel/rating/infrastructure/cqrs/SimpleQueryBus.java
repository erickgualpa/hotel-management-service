package org.egualpam.services.hotel.rating.infrastructure.cqrs;

import org.egualpam.services.hotel.rating.application.shared.Query;
import org.egualpam.services.hotel.rating.domain.hotels.HotelRepository;
import org.egualpam.services.hotel.rating.domain.reviews.ReviewRepository;

public class SimpleQueryBus implements QueryBus {

    private final HotelRepository hotelRepository;
    private final ReviewRepository reviewRepository;

    public SimpleQueryBus(HotelRepository hotelRepository, ReviewRepository reviewRepository) {
        this.hotelRepository = hotelRepository;
        this.reviewRepository = reviewRepository;
    }

    @Override
    public QueryBuilder queryBuilder() {
        return new SimpleQueryBuilder(hotelRepository, reviewRepository);
    }

    @Override
    public <T> T publish(Query<T> query) {
        return query.get();
    }
}
