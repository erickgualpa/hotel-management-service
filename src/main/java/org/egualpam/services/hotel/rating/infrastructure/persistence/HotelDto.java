package org.egualpam.services.hotel.rating.infrastructure.persistence;

import java.util.UUID;

public record HotelDto(
        UUID id,
        String name,
        String description,
        String location,
        Integer totalPrice,
        String imageURL) {
}
