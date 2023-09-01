package org.egualpam.services.hotel.rating.application;

public record HotelDto(
        String identifier,
        String name,
        String description,
        String location,
        Integer totalPrice,
        String imageURL) {
}
