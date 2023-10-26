package org.egualpam.services.hotel.rating.application.hotels;

import org.egualpam.services.hotel.rating.application.reviews.ReviewDto;

import java.util.List;

public record HotelDto(
        String identifier,
        String name,
        String description,
        String location,
        Integer totalPrice,
        String imageURL,
        List<ReviewDto> reviews) {
}
