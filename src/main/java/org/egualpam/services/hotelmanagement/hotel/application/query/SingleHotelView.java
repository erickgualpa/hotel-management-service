package org.egualpam.services.hotelmanagement.hotel.application.query;

import org.egualpam.services.hotelmanagement.shared.application.query.View;

import java.util.Optional;

public record SingleHotelView(Optional<Hotel> hotel) implements View {
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
