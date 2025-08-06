package org.egualpam.contexts.hotelmanagement.review.application.query;

import java.util.List;
import org.egualpam.contexts.hotel.shared.application.query.ReadModel;

public record ManyReviews(List<Review> reviews) implements ReadModel {
  public record Review(Integer rating, String comment) {}
}
