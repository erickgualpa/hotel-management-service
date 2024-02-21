package org.egualpam.services.hotel.rating.application.hotels;

import org.egualpam.services.hotel.rating.application.shared.View;

import java.util.List;

public record HotelsView(List<Hotel> hotels) implements View {
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
