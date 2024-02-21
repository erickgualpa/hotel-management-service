package org.egualpam.services.hotel.rating.application.reviews;

import org.egualpam.services.hotel.rating.application.shared.View;

import java.util.List;

public record ReviewsView(List<Review> reviews) implements View {
    public record Review(Integer rating, String comment) {
    }
}