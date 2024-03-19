package org.egualpam.services.hotelmanagement.application.hotels;

import org.egualpam.services.hotelmanagement.application.shared.View;

import java.util.Optional;

public record HotelView(Optional<Hotel> hotel) implements View {
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
