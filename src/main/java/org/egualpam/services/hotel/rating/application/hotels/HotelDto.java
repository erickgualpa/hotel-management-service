package org.egualpam.services.hotel.rating.application.hotels;

public record HotelDto(
        String identifier,
        String name,
        String description,
        String location,
        Integer totalPrice,
        String imageURL,
        Double averageRating
) {
}
