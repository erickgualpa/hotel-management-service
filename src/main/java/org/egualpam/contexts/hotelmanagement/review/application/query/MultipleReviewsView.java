package org.egualpam.contexts.hotelmanagement.review.application.query;

import java.util.List;
import org.egualpam.contexts.hotelmanagement.shared.application.query.View;

public record MultipleReviewsView(List<Review> reviews) implements View {
  public record Review(Integer rating, String comment) {}
}
