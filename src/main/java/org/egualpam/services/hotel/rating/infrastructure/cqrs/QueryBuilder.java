package org.egualpam.services.hotel.rating.infrastructure.cqrs;

import org.egualpam.services.hotel.rating.application.hotels.HotelDto;
import org.egualpam.services.hotel.rating.application.reviews.ReviewDto;
import org.egualpam.services.hotel.rating.application.shared.Query;

import java.util.List;
import java.util.Optional;

public interface QueryBuilder {
    Query<List<HotelDto>> findHotels(
            Optional<String> locationFilter,
            Optional<Integer> minPriceFilter,
            Optional<Integer> maxPriceFilter
    );

    Query<List<ReviewDto>> findHotelReviews(String hotelIdentifier);
}
