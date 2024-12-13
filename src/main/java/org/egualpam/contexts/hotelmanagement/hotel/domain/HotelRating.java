package org.egualpam.contexts.hotelmanagement.hotel.domain;

public record HotelRating(Integer reviewsCount, Double average) {
  public Integer ratingSum() {
    return (int) (this.reviewsCount * this.average);
  }
}
