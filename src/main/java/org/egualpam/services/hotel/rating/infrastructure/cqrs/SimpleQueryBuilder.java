package org.egualpam.services.hotel.rating.infrastructure.cqrs;

import org.egualpam.services.hotel.rating.application.hotels.FindHotelsQuery;
import org.egualpam.services.hotel.rating.application.hotels.HotelDto;
import org.egualpam.services.hotel.rating.application.reviews.FindHotelReviewsQuery;
import org.egualpam.services.hotel.rating.application.reviews.ReviewDto;
import org.egualpam.services.hotel.rating.application.shared.Query;
import org.egualpam.services.hotel.rating.domain.hotels.HotelRepository;
import org.egualpam.services.hotel.rating.domain.reviews.ReviewRepository;

import java.util.List;
import java.util.Optional;

final class SimpleQueryBuilder implements QueryBuilder {

    private final HotelRepository hotelRepository;
    private final ReviewRepository reviewRepository;

    SimpleQueryBuilder(HotelRepository hotelRepository, ReviewRepository reviewRepository) {
        this.hotelRepository = hotelRepository;
        this.reviewRepository = reviewRepository;
    }

    @Override
    public Query<List<HotelDto>> findHotels(Optional<String> locationFilter, Optional<Integer> minPriceFilter, Optional<Integer> maxPriceFilter) {
        return new FindHotelsQuery(
                locationFilter,
                minPriceFilter,
                maxPriceFilter,
                hotelRepository
        );
    }

    @Override
    public Query<List<ReviewDto>> findHotelReviews(String hotelIdentifier) {
        return new FindHotelReviewsQuery(hotelIdentifier, reviewRepository);
    }
}
