package org.egualpam.services.hotelmanagement.reviews.application;

import org.egualpam.services.hotelmanagement.shared.application.View;

import java.util.List;

public record MultipleReviewsView(List<Review> reviews) implements View {
    public record Review(Integer rating, String comment) {
    }
}