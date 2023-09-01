package org.egualpam.services.hotel.rating.infrastructure.persistance.dto;

public record Hotel(
        String id,
        String name,
        String description,
        String location,
        Integer totalPrice,
        String imageURL) {
}
