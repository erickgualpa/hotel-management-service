package org.egualpam.contexts.hotelmanagement.hotel.domain;

import static java.util.Objects.isNull;

import org.egualpam.contexts.hotelmanagement.shared.domain.RequiredPropertyIsMissing;

public record HotelRating(Integer reviewsCount, Double average) {

  public HotelRating {
    if (isNull(reviewsCount) || isNull(average)) {
      throw new RequiredPropertyIsMissing();
    }
  }

  public Integer ratingSum() {
    return (int) (this.reviewsCount * this.average);
  }
}
