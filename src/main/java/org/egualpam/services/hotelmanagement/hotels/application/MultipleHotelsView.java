package org.egualpam.services.hotelmanagement.hotels.application;

import org.egualpam.services.hotelmanagement.shared.application.query.View;

import java.util.List;

public record MultipleHotelsView(List<Hotel> hotels) implements View {
    public record Hotel(
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
