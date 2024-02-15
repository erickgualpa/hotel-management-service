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

public final class QueryFactory {

    private final HotelRepository hotelRepository;
    private final ReviewRepository reviewRepository;

    public QueryFactory(HotelRepository hotelRepository, ReviewRepository reviewRepository) {
        this.hotelRepository = hotelRepository;
        this.reviewRepository = reviewRepository;
    }

    public Query<List<HotelDto>> findHotelsQuery(
            Optional<String> locationFilter,
            Optional<Integer> minPriceFilter,
            Optional<Integer> maxPriceFilter
    ) {
        return new FindHotelsQuery(
                locationFilter,
                minPriceFilter,
                maxPriceFilter,
                hotelRepository
        );
    }

    public Query<List<ReviewDto>> findHotelReviewsQuery(String hotelIdentifier) {
        return new FindHotelReviewsQuery(hotelIdentifier, reviewRepository);
    }
}
