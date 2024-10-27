package org.egualpam.contexts.hotelmanagement.review.domain;

public record Rating(Integer value) {
  public Rating {
    if (value < 1 || value > 5) {
      throw new InvalidRating();
    }
  }
}
