package org.egualpam.services.hotelmanagement.application.hotels;

import org.egualpam.services.hotelmanagement.shared.application.View;

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
