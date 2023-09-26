package org.egualpam.services.hotel.rating.infrastructure.persistance;

public record HotelDto(
        String globalIdentifier,
        String name,
        String description,
        String location,
        Integer totalPrice,
        String imageURL) {
}
