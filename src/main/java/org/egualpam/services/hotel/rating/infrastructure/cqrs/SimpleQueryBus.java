package org.egualpam.services.hotel.rating.infrastructure.cqrs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.egualpam.services.hotel.rating.application.hotels.FindHotels;
import org.egualpam.services.hotel.rating.application.hotels.HotelDto;
import org.egualpam.services.hotel.rating.application.reviews.FindHotelReviews;
import org.egualpam.services.hotel.rating.application.reviews.ReviewDto;
import org.egualpam.services.hotel.rating.application.shared.InternalQuery;
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
        if (query instanceof FindHotelReviewsQuery findHotelReviewsQuery) {
            InternalQuery<List<ReviewDto>> internalQuery =
                    new FindHotelReviews(
                            findHotelReviewsQuery.getHotelIdentifier(),
                            reviewRepository
                    );

            List<ReviewDto> reviewsDto = internalQuery.get();

            try {
                return objectMapper.writeValueAsString(reviewsDto);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("QueryResultSerializationFailed");
            }
        } else if (query instanceof FindHotelsQuery findHotelsQuery) {
            InternalQuery<List<HotelDto>> internalQuery =
                    new FindHotels(
                            findHotelsQuery.getLocation(),
                            findHotelsQuery.getMinPrice(),
                            findHotelsQuery.getMaxPrice(),
                            hotelRepository
                    );

            List<HotelDto> hotelsDto = internalQuery.get();

            try {
                return objectMapper.writeValueAsString(hotelsDto);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("QueryResultSerializationFailed");
            }
        }
        throw new RuntimeException("QueryHandlerNotFound");
    }
}
