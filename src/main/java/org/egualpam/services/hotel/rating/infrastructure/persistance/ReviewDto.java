package org.egualpam.services.hotel.rating.infrastructure.persistance;

public record ReviewDto(
        String identifier,
        Integer rating,
        String comment) {
}