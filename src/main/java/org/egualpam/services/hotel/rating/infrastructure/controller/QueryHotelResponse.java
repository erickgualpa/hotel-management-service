package org.egualpam.services.hotel.rating.infrastructure.controller;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.List;

@JsonSerialize
public record QueryHotelResponse(List<Hotel> hotels) {
    record Hotel(
            String identifier,
            String name,
            String description,
            String location,
            Integer totalPrice,
            String imageURL,
            Double averageRating
    ) {
    }
}

