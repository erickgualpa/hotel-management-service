package org.egualpam.services.hotelmanagement.review.application.query;

import org.egualpam.services.hotelmanagement.shared.application.query.View;

import java.util.List;

public record MultipleReviewsView(List<Review> reviews) implements View {
    public record Review(Integer rating, String comment) {
    }
}