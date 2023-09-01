package org.egualpam.services.hotel.rating.infrastructure.persistance.dto;

public record Hotel(
        Long id,
        String name,
        String description,
        String location,
        Integer totalPrice,
        String imageURL) {
}
