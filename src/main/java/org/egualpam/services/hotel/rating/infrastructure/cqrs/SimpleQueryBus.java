package org.egualpam.services.hotel.rating.infrastructure.cqrs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.egualpam.services.hotel.rating.application.hotels.HotelDto;
import org.egualpam.services.hotel.rating.application.reviews.ReviewDto;
import org.egualpam.services.hotel.rating.domain.hotels.HotelRepository;
import org.egualpam.services.hotel.rating.domain.reviews.ReviewRepository;
import org.egualpam.services.hotel.rating.infrastructure.controller.FindHotelReviewsQuery;
import org.egualpam.services.hotel.rating.infrastructure.controller.FindHotelsQuery;

import java.util.List;

public final class SimpleQueryBus implements QueryBus {

    private final HotelRepository hotelRepository;
    private final ReviewRepository reviewRepository;
    private final ObjectMapper objectMapper;

    public SimpleQueryBus(
            ObjectMapper objectMapper,
            HotelRepository hotelRepository,
            ReviewRepository reviewRepository) {
        this.hotelRepository = hotelRepository;
        this.reviewRepository = reviewRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public String publish(Query query) {
        if (query instanceof FindHotelReviewsQuery) {
            org.egualpam.services.hotel.rating.application.reviews.FindHotelReviewsQuery findHotelReviewsQuery =
                    new org.egualpam.services.hotel.rating.application.reviews.FindHotelReviewsQuery(
                            ((FindHotelReviewsQuery) query).getHotelIdentifier(),
                            reviewRepository
                    );

            List<ReviewDto> reviewsDto = findHotelReviewsQuery.get();

            try {
                return objectMapper.writeValueAsString(reviewsDto);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("QueryResultSerializationFailed");
            }
        } else if (query instanceof FindHotelsQuery) {
            org.egualpam.services.hotel.rating.application.hotels.FindHotelsQuery findHotelsQuery =
                    new org.egualpam.services.hotel.rating.application.hotels.FindHotelsQuery(
                            ((FindHotelsQuery) query).getLocationFilter(),
                            ((FindHotelsQuery) query).getMinPriceFilter(),
                            ((FindHotelsQuery) query).getMaxPriceFilter(),
                            hotelRepository
                    );

            List<HotelDto> hotelsDto = findHotelsQuery.get();

            try {
                return objectMapper.writeValueAsString(hotelsDto);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("QueryResultSerializationFailed");
            }
        }
        throw new RuntimeException("QueryHandlerNotFound");
    }
}
